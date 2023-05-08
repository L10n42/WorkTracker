package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder

interface SettingsRepository {

    fun setActivityOrder(order: ActivityOrder)

    fun getActivityOrder(): ActivityOrder

    fun setVoiceNotification(enable: Boolean)

    fun getVoiceNotification(): Boolean

    fun setNotificationMsg(msg: String)

    fun getNotificationMsg(): String
}