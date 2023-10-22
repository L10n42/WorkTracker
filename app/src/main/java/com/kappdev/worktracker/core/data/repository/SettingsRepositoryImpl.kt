package com.kappdev.worktracker.core.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import com.kappdev.worktracker.tracker_feature.domain.util.OrderType
import java.time.LocalTime

class SettingsRepositoryImpl(
    context: Context
): SettingsRepository {

    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedSharedPref = EncryptedSharedPreferences.create(
        SECURED_PREFS, masterKey, context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val encryptedEditor = encryptedSharedPref.edit()

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

    override fun setReportTime(time: LocalTime) {
        val timeJson = Gson().toJson(time)
        editor.putString(REPORT_TIME_KEY, timeJson).apply()
    }

    override fun getReportTime(): LocalTime {
        val json = sharedPreferences.getString(REPORT_TIME_KEY, null)?: return DefaultReportTime
        return Gson().fromJson(json, LocalTime::class.java)
    }

    override fun setPrivacyEnable(enable: Boolean) {
        editor.putBoolean(PRIVACY_ENABLE_KEY, enable).apply()
    }

    override fun privacyEnable(): Boolean {
        return sharedPreferences.getBoolean(PRIVACY_ENABLE_KEY, false)
    }

    override fun setPassword(password: String) {
        encryptedEditor.putString(PASSWORD_KEY, password).apply()
    }

    override fun checkPassword(password: String): Boolean {
        val rightPassword = encryptedSharedPref.getString(PASSWORD_KEY, "") ?: ""
        return rightPassword == password
    }

    private data class OrderJson(val orderId: String, val typeId: String)

    companion object {
        private val DefaultOrder = ActivityOrder.Name(OrderType.Ascending)
        private val DefaultReportTime = LocalTime.of(21, 0)

        private const val SETTINGS = "settings"
        private const val SECURED_PREFS = "secured_shared_prefs"
        private const val ACTIVITY_ORDER_KEY = "ACTIVITY_ORDER_KEY"
        private const val REPORT_TIME_KEY = "REPORT_TIME_KEY"
        private const val PRIVACY_ENABLE_KEY = "PRIVACY_ENABLE_KEY"
        private const val PASSWORD_KEY = "PASSWORD_KEY"
        private const val EVERYDAY_REPORTS_ENABLE_KEY = "EVERYDAY_REPORTS_ENABLE_KEY"
    }
}