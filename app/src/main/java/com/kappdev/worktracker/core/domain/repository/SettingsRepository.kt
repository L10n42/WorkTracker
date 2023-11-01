package com.kappdev.worktracker.core.domain.repository

import android.content.SharedPreferences
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import java.time.LocalTime

interface SettingsRepository {

    val sharedPref: SharedPreferences

    fun setActivityOrder(order: ActivityOrder)

    fun getActivityOrder(): ActivityOrder

    fun setEverydayReports(enable: Boolean)

    fun everydayReportsEnable(): Boolean

    fun enableTimeTemplate(enable: Boolean)

    fun isTimeTemplateEnabled(): Boolean

    fun showServiceInfo(): Boolean

    fun viewServiceInfo()

    fun setDarkTheme(darkTheme: Boolean)

    fun isThemeDark(): Boolean

    fun setReportTime(time: LocalTime)

    fun getReportTime(): LocalTime
}