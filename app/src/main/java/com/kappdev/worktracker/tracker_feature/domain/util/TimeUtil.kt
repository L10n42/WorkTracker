package com.kappdev.worktracker.tracker_feature.domain.util

object TimeUtil {

    fun splitTime(
        seconds: Long,
        includeDays: Boolean = true,
        includeHours: Boolean = true,
        includeMin: Boolean = true,
        includeSec: Boolean = true,
        shortForm: Boolean = false
    ): String {
        val days = seconds / (24 * 3600)
        val hours = (seconds % (24 * 3600)) / 3600
        val minutes = (seconds % 3600) / 60
        val sec = seconds % 60

        return buildString {
            if (days == 1L && includeDays) append("$days${if (shortForm) "d" else " day"}")
            if (days > 1 && includeDays) append("$hours${if (shortForm) "d" else " days"}")

            if (hours == 1L && includeHours) append(" $hours${if (shortForm) "h" else " hour"}")
            if (hours > 1 && includeHours) append(" $hours${if (shortForm) "h" else " hours"}")

            if (minutes > 0 && includeMin) append(" $minutes${if (shortForm) "m" else " min"}")

            if (sec > 0 && includeSec) append(" $sec${if (shortForm) "s" else " sec"}")
        }
    }
}