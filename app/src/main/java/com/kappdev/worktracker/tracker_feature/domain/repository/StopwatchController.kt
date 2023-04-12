package com.kappdev.worktracker.tracker_feature.domain.repository

interface StopwatchController {

    fun start(activityId: Long, activityName: String)

    fun stop()

    fun resume()

    fun finish()
}