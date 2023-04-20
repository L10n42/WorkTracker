package com.kappdev.worktracker.tracker_feature.domain.util

sealed class ActivityOrder(val orderType: OrderType, val id: String) {
    class Name(orderType: OrderType): ActivityOrder(orderType, NAME_ID)

    companion object {
        private const val NAME_ID = "by_name"

        fun getById(id: String, orderType: OrderType?): ActivityOrder? {
            val type = orderType ?: OrderType.Ascending
            return when (id) {
                NAME_ID -> Name(type)
                else -> null
            }
        }
    }
}
