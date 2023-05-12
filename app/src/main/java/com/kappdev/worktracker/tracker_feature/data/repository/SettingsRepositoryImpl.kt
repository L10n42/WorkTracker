package com.kappdev.worktracker.tracker_feature.data.repository

import android.content.Context
import com.google.gson.Gson
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import com.kappdev.worktracker.tracker_feature.domain.util.OrderType
import java.time.LocalTime

class SettingsRepositoryImpl(
    private val context: Context
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

    override fun setVoiceNotification(enable: Boolean) {
        editor.putBoolean(VOICE_NOTIFICATION_KEY, enable).apply()
    }

    override fun getVoiceNotification(): Boolean {
        return sharedPreferences.getBoolean(VOICE_NOTIFICATION_KEY, true)
    }

    override fun setNotificationMsg(msg: String) {
        editor.putString(VOICE_NOTIFICATION_MSG_KEY, msg).apply()
    }

    override fun getNotificationMsg(): String {
        val defaultMsg = context.getString(R.string.default_notification_msg)
        return sharedPreferences.getString(VOICE_NOTIFICATION_MSG_KEY, null) ?: defaultMsg
    }

    override fun setEverydayReports(enable: Boolean) {
        editor.putBoolean(EVERYDAY_REPORTS_ENABLE_KEY, enable).apply()
    }

    override fun everydayReportsEnable(): Boolean {
        return sharedPreferences.getBoolean(EVERYDAY_REPORTS_ENABLE_KEY, false)
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
        private const val REPORT_TIME_KEY = "REPORT_TIME_KEY"
        private const val VOICE_NOTIFICATION_KEY = "VOICE_NOTIFICATION_KEY"
        private const val VOICE_NOTIFICATION_MSG_KEY = "VOICE_NOTIFICATION_MSG_KEY"
        private const val EVERYDAY_REPORTS_ENABLE_KEY = "EVERYDAY_REPORTS_ENABLE_KEY"
    }
}