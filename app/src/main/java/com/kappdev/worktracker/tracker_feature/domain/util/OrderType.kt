package com.kappdev.worktracker.tracker_feature.domain.util

sealed class OrderType(val id: String) {
    object Ascending: OrderType(ASCENDING_ID)
    object Descending: OrderType(DESCENDING_ID)

    companion object {
        private const val ASCENDING_ID = "ASC"
        private const val DESCENDING_ID = "DESC"

        fun getById(id: String): OrderType? {
            return when (id) {
                ASCENDING_ID -> Ascending
                DESCENDING_ID -> Descending
                else -> null
            }
        }
    }
}
