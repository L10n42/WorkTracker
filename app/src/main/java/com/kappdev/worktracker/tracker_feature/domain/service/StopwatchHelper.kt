package com.kappdev.worktracker.tracker_feature.domain.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kappdev.worktracker.MainActivity
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTION_SERVICE_START
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.ACTIVITY_ID
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.CANCEL_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.CLICK_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.RESUME_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.STOPWATCH_STATE
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants.STOP_REQUEST_CODE

object StopwatchHelper {

    private val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun startForegroundService(context: Context, activityId: Long) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = ACTION_SERVICE_START
            this.putExtra(ACTIVITY_ID, activityId)
            context.startService(this)
        }
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}