package com.kappdev.worktracker.tracker_feature.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTION_SERVICE_CANCEL
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTION_SERVICE_START
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTION_SERVICE_STOP
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTIVITY_ID
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.NOTIFICATION_CHANNEL_ID
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.NOTIFICATION_CHANNEL_NAME
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.NOTIFICATION_ID
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.STOPWATCH_STATE
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
    lateinit var notificationManager: NotificationManager

    @Inject
    @Named("StopwatchNotificationBuilder")
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    @Named("serviceActivityRepository")
    lateinit var activityRepository: ActivityRepository

    @Inject
    @Named("serviceSessionRepository")
    lateinit var sessionRepository: SessionRepository

    private val binder = StopwatchBinder()

    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(StopwatchState.Idle)
        private set
    var activityId = mutableStateOf<Long>(0)
        private set
    private var currentActivity = Activity.Empty
    private var sessionId: Long = 0

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            StopwatchState.Started.name -> {
                setStopButton()
                startForegroundService()
                startStopwatch()
            }
            StopwatchState.Stopped.name -> {
                stopStopwatch()
                setResumeButton()
            }
            StopwatchState.Canceled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    catchIdAndGetData(intent)
                    setStopButton()
                    startForegroundService()
                    startStopwatch()
                }
                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    setResumeButton()
                }
                ACTION_SERVICE_CANCEL -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun catchIdAndGetData(intent: Intent?) {
        if (activityId.value <= 0) {
            val id = intent?.getLongExtra(ACTIVITY_ID, 0)
            if (id != null && id > 0) {
                activityId.value = id
                getDataAndStartSession()
            }
        }
    }

    private fun getDataAndStartSession() {
        CoroutineScope(Dispatchers.IO).launch {
            if (activityId.value > 0) {
                currentActivity = activityRepository.getActivityById(activityId.value)
                updateNotificationTitle()
                startSession()
            }
        }
    }

    private fun startStopwatch() {
        currentState.value = StopwatchState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            updateNotification()
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = StopwatchState.Stopped
    }

    private fun cancelStopwatch() {
        saveSessionAndClearData()
        updateTimeUnits()
    }

    private fun startSession() {
        CoroutineScope(Dispatchers.IO).launch {
            sessionId = sessionRepository.insertSession(
                Session(
                    id = 0,
                    activityId = activityId.value,
                    startTimestamp = System.currentTimeMillis(),
                    endTimestamp = 0,
                    timeInSec = duration.inWholeSeconds
                )
            )
        }
    }

    private fun saveSessionAndClearData() {
        CoroutineScope(Dispatchers.IO).launch {
             sessionRepository.insertSession(
                 sessionRepository.getSessionById(sessionId).copy(
                     endTimestamp = System.currentTimeMillis(),
                     timeInSec = duration.inWholeSeconds
                 )
            )
            clearData()
        }
    }

    private fun clearData() {
        sessionId = 0
        duration = Duration.ZERO
        currentState.value = StopwatchState.Idle
        activityId.value = 0
        currentActivity = Activity.Empty
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StopwatchService.hours.value = hours.toInt().pad()
            this@StopwatchService.minutes.value = minutes.pad()
            this@StopwatchService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
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
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText(
                    "${hours.value}:${minutes.value}:${seconds.value}"
                ).build()
        )
    }

    private fun updateNotificationTitle() {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentTitle(currentActivity.name)
                .build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            index = 0,
            NotificationCompat.Action(0, "Stop", StopwatchHelper.stopPendingIntent(this))
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            index = 0,
            NotificationCompat.Action(0, "Resume", StopwatchHelper.resumePendingIntent(this))
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class StopwatchBinder: Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }
}

private fun Int.pad(): String {
    return this.toString().padStart(2,'0')
}

enum class StopwatchState {
    Idle,
    Started,
    Stopped,
    Canceled
}