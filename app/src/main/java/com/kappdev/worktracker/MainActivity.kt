package com.kappdev.worktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kappdev.worktracker.ui.theme.WorkTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkTrackerTheme(
                darkTheme = true
            ) {

            }
        }
    }
}