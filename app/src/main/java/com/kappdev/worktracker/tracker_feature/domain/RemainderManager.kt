package com.kappdev.worktracker.tracker_feature.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kappdev.worktracker.tracker_feature.data.receiver.AlarmReceiver
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class RemainderManager @Inject constructor(
    private val application: Application
) {
    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun startRemainder(time: LocalTime, id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val startTimeMillis = getStartTimeOf(time)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTimeMillis, AlarmManager.INTERVAL_DAY, intent)
    }

    fun stopRemainder(id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, PendingIntent.FLAG_IMMUTABLE)
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