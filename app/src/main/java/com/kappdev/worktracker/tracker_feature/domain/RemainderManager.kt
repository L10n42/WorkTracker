package com.kappdev.worktracker.tracker_feature.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kappdev.worktracker.tracker_feature.data.AlarmReceiver
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class RemainderManager @Inject constructor(
    private val application: Application
) {
    private val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

    fun startRemainder(time: LocalTime, id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, flag)
        }

        val startTimeMillis = getStartTimeOf(time)
        val intervalMillis = AlarmManager.INTERVAL_DAY
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTimeMillis, intervalMillis, intent)
    }

    fun stopRemainder(id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, flag)
        }

        alarmManager.cancel(intent)
    }

    private fun getStartTimeOf(time: LocalTime): Long {
        val rightTime = time.withSecond(0)
        val currentDate = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDate()
        val zonedDateTime = rightTime.atDate(currentDate).atZone(ZoneId.systemDefault())

        return zonedDateTime.toInstant().toEpochMilli()
    }

    companion object {
        private const val REMAINDER_NOTIFICATION_REQUEST_CODE = 14657
    }
}