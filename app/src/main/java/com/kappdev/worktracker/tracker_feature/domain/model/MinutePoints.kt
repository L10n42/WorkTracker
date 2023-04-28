package com.kappdev.worktracker.tracker_feature.domain.model

data class MinutePoints(
    val timestamps: List<Long>
) {
    companion object {
        val Empty = MinutePoints(emptyList())
    }
}


