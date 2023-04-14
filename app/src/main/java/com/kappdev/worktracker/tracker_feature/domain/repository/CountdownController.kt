package com.kappdev.worktracker.tracker_feature.domain.repository

interface CountdownController {

    fun start(activityId: Long, activityName: String, durationInMillis: Long)

    fun stop()

    fun resume()

    fun finish()
}