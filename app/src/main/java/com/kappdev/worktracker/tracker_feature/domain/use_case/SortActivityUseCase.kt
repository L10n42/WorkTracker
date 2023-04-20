package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import com.kappdev.worktracker.tracker_feature.domain.util.OrderType

class SortActivityUseCase {

    operator fun invoke(activities: List<Activity>, order: ActivityOrder): List<Activity> {
        return when(order.orderType) {
            is OrderType.Ascending -> {
                when(order) {
                    is ActivityOrder.Name -> activities.sortedBy { it.name.lowercase() }
                }
            }
            is OrderType.Descending -> {
                when(order) {
                    is ActivityOrder.Name -> activities.sortedByDescending { it.name.lowercase() }
                }
            }
        }
    }
}