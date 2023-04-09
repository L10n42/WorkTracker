package com.kappdev.worktracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kappdev.worktracker.core.navigation.SetupNavGraph
import com.kappdev.worktracker.tracker_feature.domain.service.StopwatchService
import com.kappdev.worktracker.ui.theme.WorkTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var systemUiController: SystemUiController
    private lateinit var stopwatchService: StopwatchService
    private var isBound by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkTrackerTheme(
                darkTheme = true
            ) {
                navController = rememberNavController()
                systemUiController = rememberSystemUiController()

                val backgroundColor = MaterialTheme.colors.background
                val surfaceColor = MaterialTheme.colors.surface
                SideEffect {
                    systemUiController.setStatusBarColor(surfaceColor)
                    systemUiController.setNavigationBarColor(backgroundColor)
                }

                if (isBound) {
                    SetupNavGraph(navController, stopwatchService)
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}