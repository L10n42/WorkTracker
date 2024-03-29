package com.kappdev.worktracker.tracker_feature.data.repository

import android.content.Context
import android.content.Intent
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.data.util.ServiceConstants

class StopwatchControllerImpl(
    private val context: Context
): StopwatchController {

    override fun start(activityId: Long, activityName: String) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = ServiceConstants.ACTION_SERVICE_START
            this.putExtra(ServiceConstants.ACTIVITY_ID, activityId)
            this.putExtra(ServiceConstants.ACTIVITY_NAME, activityName)
            context.startService(this)
        }
    }

    override fun stop() = triggerServiceWith(ServiceConstants.ACTION_SERVICE_STOP)

    override fun resume() = triggerServiceWith(ServiceConstants.ACTION_SERVICE_START)

    override fun finish() = triggerServiceWith(ServiceConstants.ACTION_SERVICE_CANCEL)

    private fun triggerServiceWith(action: String) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}