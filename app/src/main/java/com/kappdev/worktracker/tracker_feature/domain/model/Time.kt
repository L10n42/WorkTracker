package com.kappdev.worktracker.tracker_feature.domain.model

data class Time(
    val hours: String = "00",
    val minutes: String = "00",
    val seconds: String = "00",
) {
    companion object {
        fun from(seconds: Long): Time {
            val rs = seconds % 3600
            return from((seconds / 3600), (rs / 60), (rs % 60))
        }

        fun from(hours: Number, minutes: Number, seconds: Number): Time {
            return Time(
                hours = hours.toString().padStart(2,'0'),
                minutes = minutes.toString().padStart(2,'0'),
                seconds = seconds.toString().padStart(2,'0'),
            )
        }
    }
}

fun Time.isNotEmpty() = !this.isEmpty()

fun Time.isEmpty() = (this.getHours() < 0) && (this.getMinutes() < 0) && (this.getSeconds() < 0)

fun Time.inMillis() = this.inSeconds() * 1000L

fun Time.inSeconds() = (this.getHours() * 3600) + (this.getMinutes() * 60) + this.getSeconds().toLong()

fun Time.getHours() = this.hours.toInt()

fun Time.getMinutes() = this.minutes.toInt()

fun Time.getSeconds() = this.seconds.toInt()

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