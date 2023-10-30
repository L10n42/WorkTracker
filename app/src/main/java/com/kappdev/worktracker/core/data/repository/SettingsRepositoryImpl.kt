package com.kappdev.worktracker.core.data.repository

import android.content.Context
import com.google.gson.Gson
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import com.kappdev.worktracker.tracker_feature.domain.util.OrderType
import java.time.LocalTime

class SettingsRepositoryImpl(
    context: Context
): SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    override fun setActivityOrder(order: ActivityOrder) {
        val orderJson = OrderJson(order.id, order.orderType.id)
        editor.putString(ACTIVITY_ORDER_KEY, Gson().toJson(orderJson)).apply()
    }

    override fun getActivityOrder(): ActivityOrder {
        val json = sharedPreferences.getString(ACTIVITY_ORDER_KEY, null)?: return DefaultOrder
        val jsonOrder = Gson().fromJson(json, OrderJson::class.java)
        val orderType = OrderType.getById(jsonOrder.typeId)
        return ActivityOrder.getById(jsonOrder.orderId, orderType)?: DefaultOrder
    }

    override fun setEverydayReports(enable: Boolean) {
        editor.putBoolean(EVERYDAY_REPORTS_ENABLE_KEY, enable).apply()
    }

    override fun everydayReportsEnable(): Boolean {
        return sharedPreferences.getBoolean(EVERYDAY_REPORTS_ENABLE_KEY, false)
    }

    override fun enableTimeTemplate(enable: Boolean) {
        editor.putBoolean(TIME_TEMPLATE_KEY, enable).apply()
    }

    override fun isTimeTemplateEnabled(): Boolean {
        return sharedPreferences.getBoolean(TIME_TEMPLATE_KEY, false)
    }

    override fun setReportTime(time: LocalTime) {
        val timeJson = Gson().toJson(time)
        editor.putString(REPORT_TIME_KEY, timeJson).apply()
    }

    override fun getReportTime(): LocalTime {
        val json = sharedPreferences.getString(REPORT_TIME_KEY, null)?: return DefaultReportTime
        return Gson().fromJson(json, LocalTime::class.java)
    }

    private data class OrderJson(val orderId: String, val typeId: String)

    companion object {
        private val DefaultOrder = ActivityOrder.Name(OrderType.Ascending)
        private val DefaultReportTime = LocalTime.of(21, 0)

        private const val SETTINGS = "settings"
        private const val ACTIVITY_ORDER_KEY = "ACTIVITY_ORDER_KEY"
        private const val TIME_TEMPLATE_KEY = "TIME_TEMPLATE_KEY"
        private const val REPORT_TIME_KEY = "REPORT_TIME_KEY"
        private const val EVERYDAY_REPORTS_ENABLE_KEY = "EVERYDAY_REPORTS_ENABLE_KEY"
    }
}