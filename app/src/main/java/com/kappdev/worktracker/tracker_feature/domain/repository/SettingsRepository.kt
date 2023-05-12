package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import java.time.LocalTime

interface SettingsRepository {

    fun setActivityOrder(order: ActivityOrder)

    fun getActivityOrder(): ActivityOrder

    fun setVoiceNotification(enable: Boolean)

    fun getVoiceNotification(): Boolean

    fun setNotificationMsg(msg: String)

    fun getNotificationMsg(): String

    fun setEverydayReports(enable: Boolean)

    fun everydayReportsEnable(): Boolean

    fun setReportTime(time: LocalTime)

    fun getReportTime(): LocalTime
}