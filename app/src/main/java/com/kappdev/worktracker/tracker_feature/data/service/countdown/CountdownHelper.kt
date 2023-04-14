package com.kappdev.worktracker.tracker_feature.data.service.countdown

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kappdev.worktracker.MainActivity
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState

object CountdownHelper {
    private val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(ServiceConstants.SERVICE_STATE, ServiceState.Started.name)
        }
        return PendingIntent.getActivity(
            context, ServiceConstants.CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, CountdownService::class.java).apply {
            putExtra(ServiceConstants.SERVICE_STATE, ServiceState.Stopped.name)
        }
        return PendingIntent.getService(
            context, ServiceConstants.STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, CountdownService::class.java).apply {
            putExtra(ServiceConstants.SERVICE_STATE, ServiceState.Started.name)
        }
        return PendingIntent.getService(
            context, ServiceConstants.RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, CountdownService::class.java).apply {
            putExtra(ServiceConstants.SERVICE_STATE, ServiceState.Canceled.name)
        }
        return PendingIntent.getService(
            context, ServiceConstants.CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }
}