package com.kappdev.worktracker.tracker_feature.domain.util

object TimeUtil {

    fun toTimeFormat(timestamp: Long): String {
        val hours = timestamp / 3600
        val remainingSeconds = timestamp % 3600

        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return buildString {
            if (hours == 1L) append("$hours hour")
            if (hours > 1) append("$hours hours")
            if (minutes > 0) append(" $minutes min")
            if (seconds > 0) append(" $seconds sec")
        }
    }
}