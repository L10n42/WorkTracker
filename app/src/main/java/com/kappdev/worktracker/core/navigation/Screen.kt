package com.kappdev.worktracker.core.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object AddEditActivity: Screen("add_edit_activity_screen")
    object ActivityReview: Screen("activity_review_screen")
    object StopwatchTimer: Screen("stopwatch_timer_screen")
    object CountdownTimer: Screen("countdown_timer_screen")
}
