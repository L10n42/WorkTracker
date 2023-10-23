package com.kappdev.worktracker.tracker_feature.domain.util

sealed class ActivityOrder(val orderType: OrderType, val id: String) {
    class Name(orderType: OrderType): ActivityOrder(orderType, NAME_ID)
    class Date(orderType: OrderType): ActivityOrder(orderType, DATE_ID)

    fun copy(orderType: OrderType): ActivityOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Date -> Date(orderType)
        }
    }

    companion object {
        private const val NAME_ID = "activity_name"
        private const val DATE_ID = "creation_timestamp"

        fun getById(id: String, orderType: OrderType?): ActivityOrder? {
            val type = orderType ?: OrderType.Ascending
            return when (id) {
                NAME_ID -> Name(type)
                DATE_ID -> Date(type)
                else -> null
            }
        }
    }
}
