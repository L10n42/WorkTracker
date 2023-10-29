package com.kappdev.worktracker.core.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import java.time.LocalTime

interface SettingsRepository {

    fun setActivityOrder(order: ActivityOrder)

    fun getActivityOrder(): ActivityOrder

    fun setEverydayReports(enable: Boolean)

    fun everydayReportsEnable(): Boolean

    fun setReportTime(time: LocalTime)

    fun getReportTime(): LocalTime
}