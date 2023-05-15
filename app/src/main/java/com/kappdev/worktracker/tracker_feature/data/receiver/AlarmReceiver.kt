package com.kappdev.worktracker.tracker_feature.data.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kappdev.worktracker.MainActivity
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.model.ReportData
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetDailyReportFor
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    @Named("SingletonNotificationManager")
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var getDailyReportFor: GetDailyReportFor

    override fun onReceive(context: Context, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val todayReport = getTodayReport()
            val content = getContentFrom(todayReport)
            val title = getTitleFrom(todayReport)

            sendReportNotification(context = context, content = content, title = title)
        }
    }

    private fun sendReportNotification(context: Context, title: String, content: String) {
        val contentIntent = Intent(context, MainActivity::class.java)
        contentIntent.putExtra(IS_REPORT_INTENT_EXTRA, true)
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent = PendingIntent.getActivity(context, ACTIVITY_REQUEST_CODE, contentIntent, flag)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(content)
            setAutoCancel(true)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setSmallIcon(R.drawable.baseline_assignment_24)
            setStyle(
                NotificationCompat.BigTextStyle().bigText(content)
            )
            setContentIntent(pendingIntent)
            setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            setDefaults(NotificationCompat.DEFAULT_SOUND)
            setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getTitleFrom(reportData: List<ReportData>): String {
        return buildString {
            val totalSec = reportData.sumOf { it.timeValue }
            val totalTime = TimeUtil.splitTime(totalSec)
            if (totalSec > 0) {
                append("Today's total time: $totalTime")
            } else {
                append("You didn't work today \uD83D\uDE14")
            }
        }
    }

    private fun getContentFrom(reportData: List<ReportData>): String {
        return buildString {
            reportData.forEachIndexed { index, report ->
                val name = report.activity.name
                val time = TimeUtil.splitTime(report.timeValue, shortForm = true)
                val percent = String.format("%.1f", (report.percent * 100))
                val number = index + 1

                append("$number) $name - $time ($percent%)")
                if (index != reportData.lastIndex) append("\n")
            }
        }
    }

    private fun getTodayReport() = getDailyReportFor(LocalDate.now())

    companion object {
        const val IS_REPORT_INTENT_EXTRA = "REPORT_INTENT_EXTRA"
        const val NOTIFICATION_CHANNEL_ID = "DAILY_REPORTS_NOTIFICATION_ID"
        const val NOTIFICATION_CHANNEL_NAME = "DAILY_REPORT_NOTIFICATION"
        const val ACTIVITY_REQUEST_CODE = 1237

        private const val NOTIFICATION_ID = 320750
    }
}