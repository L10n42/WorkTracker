package com.kappdev.worktracker.tracker_feature.domain.model

data class Time(
    val hours: String = "00",
    val minutes: String = "00",
    val seconds: String = "00",
) {
    companion object {
        fun from(hours: Number, minutes: Number, seconds: Number): Time {
            return Time(
                hours = hours.toString().padStart(2,'0'),
                minutes = minutes.toString().padStart(2,'0'),
                seconds = seconds.toString().padStart(2,'0'),
            )
        }
    }
}

fun Time.stringFormat(): String {
    return buildString {
        val hour = this@stringFormat.hours.toInt()
        val min = this@stringFormat.minutes.toInt()
        val sec = this@stringFormat.seconds.toInt()
        if (hour == 1) append("$hour hour")
        if (hour > 1) append("$hour hours")
        if (min > 0) append(" $min min")
        if (sec > 0) append(" $sec sec")
    }
}

fun Time.format(): String {
    return "${this.hours}:${this.minutes}:${this.seconds}"
}