package com.kappdev.worktracker.tracker_feature.domain.use_case

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kappdev.worktracker.MainActivity
import com.kappdev.worktracker.R
import javax.inject.Inject

class DoneNotification @Inject constructor(
    private val context: Application,
    private val notificationManager: NotificationManager
) {
    private val doneSoundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.achievement_bell}")

    fun makeNotification(
        title: String,
        shortMessage: CharSequence,
        fullMessage: CharSequence = shortMessage
    ) {
        val builder = buildNotification(title, shortMessage, fullMessage)

        createNotificationChannel()
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun buildNotification(
        title: CharSequence,
        shortMessage: CharSequence,
        fullMessage: CharSequence
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_done_all_24)
            setContentTitle(title)
            setContentText(shortMessage)
            setAutoCancel(true)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setStyle(bigTextFrom(fullMessage))
            setContentIntent(openAppIntent())
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSound(doneSoundUri)
            priority = NotificationCompat.PRIORITY_HIGH
            setVibrate(longArrayOf(300, 100, 300))
            setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }
    }

    private fun bigTextFrom(msg: CharSequence) = NotificationCompat.BigTextStyle().bigText(msg)

    private fun openAppIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context, ACTIVITY_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
                vibrationPattern = longArrayOf(300, 100, 300)
                setSound(
                    /* sound = */ doneSoundUri,
                    /* audioAttributes = */ AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "TIMER_COMPLETE_NOTIFICATION_ID"
        const val NOTIFICATION_CHANNEL_NAME = "TIMER_COMPLETE_NOTIFICATION"
        const val NOTIFICATION_ID = 145
        const val ACTIVITY_REQUEST_CODE = 1780
    }
}