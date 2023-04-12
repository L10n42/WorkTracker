package com.kappdev.worktracker.tracker_feature.domain.model

data class Time(
    val hours: String = "00",
    val minutes: String = "00",
    val seconds: String = "00",
)

fun Time.format(): String {
    return "${this.hours}:${this.minutes}:${this.seconds}"
}