package com.kappdev.worktracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kappdev.worktracker.core.navigation.SetupNavGraph
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.countdonw_timer.componets.CountdownBar
import com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components.StopwatchBar
import com.kappdev.worktracker.ui.theme.WorkTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    @Inject
    lateinit var stopwatchController: StopwatchController
    @Inject
    lateinit var countdownController: CountdownController

    private lateinit var navController: NavHostController
    private lateinit var systemUiController: SystemUiController
    private lateinit var stopwatchService: StopwatchService
    private lateinit var countdownService: CountdownService
    private var isStopwatchBound by mutableStateOf(false)
    private var isCountdownBound by mutableStateOf(false)

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkTrackerTheme(
                darkTheme = true
            ) {
                navController = rememberAnimatedNavController()
                systemUiController = rememberSystemUiController()

                val backgroundColor = MaterialTheme.colors.background
                val surfaceColor = MaterialTheme.colors.surface
                SideEffect {
                    systemUiController.setStatusBarColor(surfaceColor)
                    systemUiController.setNavigationBarColor(backgroundColor)
                }

                if (isStopwatchBound && isCountdownBound) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SetupNavGraph(
                            navController = navController,
                            stopwatchService = stopwatchService,
                            countdownService = countdownService,
                            stopwatchController = stopwatchController,
                            countdownController = countdownController
                        )
                        StopwatchBar(navController, stopwatchService, stopwatchController)
                        CountdownBar(navController, countdownService, countdownController)
                    }
                }
            }
        }
    }

    private val countdownConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val countdownBinder = service as CountdownService.CountdownBinder
            countdownService = countdownBinder.getService()
            isCountdownBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isCountdownBound = false
        }
    }

    private val stopwatchConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val stopwatchBinder = service as StopwatchService.StopwatchBinder
            stopwatchService = stopwatchBinder.getService()
            isStopwatchBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isStopwatchBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, stopwatchConnection, Context.BIND_AUTO_CREATE)
        }
        Intent(this, CountdownService::class.java).also { intent ->
            bindService(intent, countdownConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(stopwatchConnection)
        unbindService(countdownConnection)
        isStopwatchBound = false
        isCountdownBound = false
    }
}