package com.kappdev.worktracker.tracker_feature.data.service.stopwatch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kappdev.worktracker.MainActivity
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.CANCEL_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.CLICK_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.RESUME_REQUEST_CODE
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.SERVICE_STATE
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants.STOP_REQUEST_CODE

object StopwatchHelper {

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(SERVICE_STATE, ServiceState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(SERVICE_STATE, ServiceState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(SERVICE_STATE, ServiceState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(SERVICE_STATE, ServiceState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }
}