package com.kappdev.worktracker.tracker_feature.domain.util

sealed class ActivityOrder(val orderType: OrderType, val id: String) {
    class Name(orderType: OrderType): ActivityOrder(orderType, NAME_ID)
    class Time(orderType: OrderType): ActivityOrder(orderType, TIME_ID)

    fun copy(orderType: OrderType): ActivityOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Time -> Time(orderType)
        }
    }

    companion object {
        private const val NAME_ID = "name"
        private const val TIME_ID = "creation_timestamp"

        fun getById(id: String, orderType: OrderType?): ActivityOrder? {
            val type = orderType ?: OrderType.Ascending
            return when (id) {
                NAME_ID -> Name(type)
                TIME_ID -> Time(type)
                else -> null
            }
        }
    }
}
