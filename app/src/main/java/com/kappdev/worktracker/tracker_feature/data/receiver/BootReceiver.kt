package com.kappdev.worktracker.tracker_feature.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kappdev.worktracker.tracker_feature.domain.RemainderManager
import com.kappdev.worktracker.tracker_feature.domain.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    @Named("AppSettingsRep")
    lateinit var settings: SettingsRepository

    @Inject
    lateinit var remainderManager: RemainderManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED" && settings.everydayReportsEnable()) {
            remainderManager.startRemainder(settings.getReportTime())
        }
    }
}