package com.kappdev.worktracker.tracker_feature.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kappdev.worktracker.tracker_feature.data.receiver.AlarmReceiver
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

class RemainderManager @Inject constructor(
    private val application: Application
) {
    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun startRemainder(time: LocalTime, id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTime(time), intent)
    }

    fun stopRemainder(id: Int = REMAINDER_NOTIFICATION_REQUEST_CODE) {
        val intent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, id, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.cancel(intent)
    }

    private fun getTime(time: LocalTime): Long {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)

            if (time.isAfter(LocalTime.now())) {
                add(Calendar.DAY_OF_YEAR, 0)
            } else {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }.timeInMillis
    }

    companion object {
        private const val REMAINDER_NOTIFICATION_REQUEST_CODE = 14657
    }
}