package com.kappdev.worktracker.tracker_feature.data.service.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.data.util.NotificationButton
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.ACTION_SERVICE_CANCEL
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.ACTION_SERVICE_START
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.ACTION_SERVICE_STOP
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.ACTIVITY_ID
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.ACTIVITY_NAME
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.SERVICE_STATE
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.domain.model.format
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
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

@AndroidEntryPoint
class StopwatchService: Service() {

    @Inject
    @Named("ServiceNotificationManager")
    lateinit var notificationManager: NotificationManager

    @Inject
    @Named("serviceSessionRepository")
    lateinit var sessionRepository: SessionRepository

    private val binder = StopwatchBinder()
    private var wakeLock: PowerManager.WakeLock? = null
    private var duration: Duration = Duration.ZERO
    private var sessionId: Long = 0

    private lateinit var timer: Timer
    private lateinit var saveTimer: Timer
    private lateinit var builder: NotificationCompat.Builder

    var time = mutableStateOf(Time())
        private set
    var currentState = mutableStateOf(ServiceState.Idle)
        private set
    var activityId = mutableStateOf<Long>(0)
        private set
    var activityName = mutableStateOf("")
        private set

    override fun onCreate() {
        super.onCreate()
        builder = defaultNotificationBuilder()
    }

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(SERVICE_STATE)) {
            ServiceState.Started.name -> {
                startSession()
                setButton(NotificationButton.Stop)
                startForegroundService()
                startStopwatch()
            }
            ServiceState.Stopped.name -> {
                stopStopwatch()
                setButton(NotificationButton.Resume)
            }
            ServiceState.Canceled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    if (activityId.value <= 0) {
                        updateDataFrom(intent)
                        startSession()
                    }
                    setButton(NotificationButton.Stop)
                    startForegroundService()
                    startStopwatch()
                }
                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    setButton(NotificationButton.Resume)
                }
                ACTION_SERVICE_CANCEL -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }

        return START_STICKY
    }

    private fun updateDataFrom(intent: Intent?) {
        val id = intent?.getLongExtra(ACTIVITY_ID, 0)
        val name = intent?.getStringExtra(ACTIVITY_NAME)

        if (id != null && id > 0) activityId.value = id
        if (!name.isNullOrEmpty()) activityName.value = name
    }

    private fun startStopwatch() {
        currentState.value = ServiceState.Started
        timer = fixedRateTimer(initialDelay = 0L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            updateNotification()
        }
        startSaveTimer()
    }

    private fun startSaveTimer() {
        saveTimer = fixedRateTimer(initialDelay = 60_000L, period = 60_000L) {
            saveSession()
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) timer.cancel()
        if (this::saveTimer.isInitialized) saveTimer.cancel()
        currentState.value = ServiceState.Stopped
        saveSession()
    }

    private fun cancelStopwatch() {
        saveSession(onFinish = this::clearData)
        updateTimeUnits()
    }

    private fun startSession() = CoroutineScope(Dispatchers.IO).launch {
        sessionId = sessionRepository.startSessionFor(activityId.value)
    }

    private fun saveSession(onFinish: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            sessionRepository.saveSession(
                id = sessionId,
                timeInSec = duration.inWholeSeconds,
            )
            onFinish()
        }
    }

    private fun clearData() {
        sessionId = 0
        duration = Duration.ZERO
        currentState.value = ServiceState.Idle
        activityId.value = 0
        activityName.value = ""
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            time.value = Time.from(hours, minutes, seconds)
        }
    }

    private fun startForegroundService() {
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StopwatchService::lock").apply {
                acquire()
                acquire(10*60*1000L /*10 minutes*/)
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
            Log.e("StopwatchService","Service stopped without being started: ${e.message}")
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
                StopwatchHelper.stopPendingIntent(this)
            )
            NotificationButton.Resume -> builder.addAction(0, this.getString(R.string.btn_resume),
                StopwatchHelper.resumePendingIntent(this)
            )
        }
        builder.addAction(0, this.getString(R.string.btn_finish), StopwatchHelper.cancelPendingIntent(this))
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun defaultNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(activityName.value)
            .setContentText(time.value.format())
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setOngoing(true)
            .setContentIntent(StopwatchHelper.clickPendingIntent(this))
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "STOPWATCH_NOTIFICATION_ID"
        const val NOTIFICATION_CHANNEL_NAME = "STOPWATCH_NOTIFICATION"
        const val NOTIFICATION_ID = 16
    }

    inner class StopwatchBinder: Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }
}