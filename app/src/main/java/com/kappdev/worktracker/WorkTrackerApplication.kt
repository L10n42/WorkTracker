package com.kappdev.worktracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kappdev.worktracker.tracker_feature.data.receiver.AlarmReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WorkTrackerApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        createDailyReportChannel()
    }

    private fun createDailyReportChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlarmReceiver.NOTIFICATION_CHANNEL_ID,
                AlarmReceiver.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                )
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
}