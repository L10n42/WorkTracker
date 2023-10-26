package com.kappdev.worktracker.tracker_feature.data.service.countdown

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.PowerManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.data.util.NotificationButton
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.model.*
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import com.kappdev.worktracker.tracker_feature.domain.use_case.DoneNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@AndroidEntryPoint
class CountdownService: Service() {

    @Inject
    @Named("ServiceNotificationManager")
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var doneNotification: DoneNotification

    @Inject
    @Named("serviceSessionRepository")
    lateinit var sessionRepository: SessionRepository

    private val binder = CountdownBinder()
    private var wakeLock: PowerManager.WakeLock? = null
    private var duration: Duration = Duration.ZERO
    private var wholeDuration: Long = 0
    private var sessionId: Long = 0

    private lateinit var timer: CountDownTimer
    private lateinit var saveTimer: Timer
    private lateinit var builder: NotificationCompat.Builder

    var time = mutableStateOf(Time())
        private set
    var totalTime = mutableStateOf(Time())
        private set
    var currentState = mutableStateOf(ServiceState.Idle)
        private set
    var activityId = mutableStateOf<Long>(0)
        private set
    var activityName = mutableStateOf("")
        private set
    var completionPercentage = mutableStateOf(0f)
        private set

    override fun onBind(intent: Intent?) = binder

    override fun onCreate() {
        super.onCreate()
        builder = defaultNotificationBuilder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(ServiceConstants.SERVICE_STATE)) {
            ServiceState.Started.name -> {
                startSession()
                setButton(NotificationButton.Stop)
                startForegroundService()
                startCountdown()
            }
            ServiceState.Stopped.name -> {
                stopCountdown()
                setButton(NotificationButton.Resume)
            }
            ServiceState.Canceled.name -> {
                stopCountdown()
                cancelCountdown()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                ServiceConstants.ACTION_SERVICE_START -> {
                    if (activityId.value <= 0) {
                        updateDataFrom(intent)
                        startSession()
                    }
                    setButton(NotificationButton.Stop)
                    startForegroundService()
                    startCountdown()
                }
                ServiceConstants.ACTION_SERVICE_STOP -> {
                    stopCountdown()
                    setButton(NotificationButton.Resume)
                }
                ServiceConstants.ACTION_SERVICE_CANCEL -> {
                    stopCountdown()
                    cancelCountdown()
                    stopForegroundService()
                }
            }
        }

        return START_STICKY
    }

    private fun updateDataFrom(intent: Intent?) {
        val id = intent?.getLongExtra(ServiceConstants.ACTIVITY_ID, 0)
        val name = intent?.getStringExtra(ServiceConstants.ACTIVITY_NAME)
        val timerDuration = intent?.getLongExtra(ServiceConstants.DURATION, 0)

        if (id != null && id > 0) activityId.value = id
        if (!name.isNullOrEmpty()) activityName.value = name
        if (timerDuration != null) {
            duration = timerDuration.toDuration(DurationUnit.MILLISECONDS)
            wholeDuration = timerDuration
            setTotalTime()
        }
    }

    private fun startCountdown() {
        currentState.value = ServiceState.Started
        timer = object : CountDownTimer(duration.inWholeMilliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                duration = duration.minus(1.seconds)
                updateTimeUnits()
                updateCompletionPercentage()
                updateNotification()
            }

            override fun onFinish() {
                stopCountdown()
                makeFinishNotification()
                cancelCountdown()
                stopForegroundService()
            }
        }
        timer.start()
        startSaveTimer()
    }

    private fun startSaveTimer() {
        saveTimer = fixedRateTimer(initialDelay = 60_000L, period = 60_000L) {
            saveSession()
        }
    }

    private fun makeFinishNotification() {
        doneNotification.makeNotification(
            title = this.getString(R.string.activity_work_finished, activityName.value),
            shortMessage = this.getString(R.string.total_time, totalTime.value.stringFormat())
        )
    }

    private fun stopCountdown() {
        if (this::timer.isInitialized) timer.cancel()
        if (this::saveTimer.isInitialized) saveTimer.cancel()
        currentState.value = ServiceState.Stopped
        saveSession()
    }

    private fun cancelCountdown() {
        saveSession(onFinish = this::clearData)
        updateTimeUnits()
        updateCompletionPercentage()
    }

    private fun startSession() = CoroutineScope(Dispatchers.IO).launch {
        sessionId = sessionRepository.startSessionFor(activityId.value)
    }

    private fun saveSession(onFinish: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            sessionRepository.saveSession(sessionId)
            onFinish()
        }
    }

    private fun clearData() {
        sessionId = 0
        duration = Duration.ZERO
        wholeDuration = 0
        currentState.value = ServiceState.Idle
        activityId.value = 0
        activityName.value = ""
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            time.value = Time.from(hours, minutes, seconds)
        }
    }

    private fun setTotalTime() {
        duration.toComponents { hours, minutes, seconds, _ ->
            totalTime.value = Time.from(hours, minutes, seconds)
        }
    }

    private fun updateCompletionPercentage() {
        completionPercentage.value = duration.inWholeMilliseconds.toFloat() / wholeDuration.toFloat()
    }

    private fun startForegroundService() {
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CountdownService::lock").apply {
                acquire()
            }
        }

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, builder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.e("CountdownService","Service stopped without being started: ${e.message}")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification() {
        builder.setContentText(time.value.format())
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun setButton(button: NotificationButton) {
        builder = defaultNotificationBuilder()
        when (button) {
            NotificationButton.Stop -> builder.addAction(0, this.getString(R.string.btn_pause),
                CountdownHelper.stopPendingIntent(this)
            )
            NotificationButton.Resume -> builder.addAction(0, this.getString(R.string.btn_resume),
                CountdownHelper.resumePendingIntent(this)
            )
        }
        builder.addAction(0, this.getString(R.string.btn_finish), CountdownHelper.cancelPendingIntent(this))
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun defaultNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(activityName.value)
            .setContentText(time.value.format())
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setOngoing(true)
            .setContentIntent(CountdownHelper.clickPendingIntent(this))
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "COUNTDOWN_NOTIFICATION_ID"
        private const val NOTIFICATION_CHANNEL_NAME = "COUNTDOWN_NOTIFICATION"
        private const val NOTIFICATION_ID = 32
    }

    inner class CountdownBinder: Binder() {
        fun getService(): CountdownService = this@CountdownService
    }
}