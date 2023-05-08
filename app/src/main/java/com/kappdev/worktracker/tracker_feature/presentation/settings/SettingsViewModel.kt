package com.kappdev.worktracker.tracker_feature.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kappdev.worktracker.tracker_feature.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Named("AppSettingsRep") private val settings: SettingsRepository
) : ViewModel() {

    private val _voiceNotification = mutableStateOf(settings.getVoiceNotification())
    val voiceNotification: State<Boolean> = _voiceNotification

    private val _notificationMsg = mutableStateOf(settings.getNotificationMsg())
    val notificationMsg: State<String> = _notificationMsg

    fun setNotificationMsg(msg: String) {
        _notificationMsg.value = msg
        settings.setNotificationMsg(msg)
    }

    fun setVoiceNotification(enable: Boolean) {
        _voiceNotification.value = enable
        settings.setVoiceNotification(enable)
    }
}