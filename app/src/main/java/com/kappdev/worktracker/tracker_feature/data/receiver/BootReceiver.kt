package com.kappdev.worktracker.tracker_feature.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.RemainderManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    @Named("SingletonAppSettingsRep")
    lateinit var settings: SettingsRepository

    @Inject
    lateinit var remainderManager: RemainderManager

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null && action == Intent.ACTION_BOOT_COMPLETED) {
            if (settings.everydayReportsEnable()) {
                remainderManager.startRemainder(settings.getReportTime())
            }
        }
    }
}