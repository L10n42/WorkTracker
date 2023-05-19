package com.kappdev.worktracker.tracker_feature.presentation.settings

data class PasswordState(
    val first: String = "",
    val second: String = ""
)

fun PasswordState.areSame(): Boolean {
    return this.first == this.second
}

fun PasswordState.isSuitableLength(): Boolean {
    return this.first.length > 3 && this.second.length > 3
}

fun PasswordState.isNotBlank(): Boolean {
    return this.first.isNotBlank() && this.second.isNotBlank()
}