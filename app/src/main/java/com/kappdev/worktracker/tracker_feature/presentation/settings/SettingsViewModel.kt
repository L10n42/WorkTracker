package com.kappdev.worktracker.tracker_feature.presentation.settings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kappdev.worktracker.tracker_feature.domain.RemainderManager
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Named("AppSettingsRep") private val settings: SettingsRepository,
    private val remainderManager: RemainderManager
) : ViewModel() {

    private val _voiceNotification = mutableStateOf(settings.getVoiceNotification())
    val voiceNotification: State<Boolean> = _voiceNotification

    private val _notificationMsg = mutableStateOf(settings.getNotificationMsg())
    val notificationMsg: State<String> = _notificationMsg

    private val _everydayReportEnable = mutableStateOf(settings.everydayReportsEnable())
    val everydayReportEnable: State<Boolean> = _everydayReportEnable

    private val _reportTime = mutableStateOf(settings.getReportTime())
    val reportTime: State<LocalTime> = _reportTime

    private val _privacyEnable = mutableStateOf(settings.privacyEnable())
    val privacyEnable: State<Boolean> = _privacyEnable

    private val _password = mutableStateOf(PasswordState())
    val password: State<PasswordState> = _password

    fun isPasswordCorrect() = password.value.areSame()

    fun updatePassword() {
        if (privacyEnable.value) {
            settings.setPassword(password.value.first)
        } else {
            settings.setPassword("")
        }

    }

    fun setPassword(password: PasswordState) {
        _password.value = password
    }

    fun cancelRemainder() = remainderManager.stopRemainder()

    fun updateRemainder() {
        remainderManager.startRemainder(reportTime.value)
    }

    fun setPrivacyEnable(enable: Boolean) {
        _privacyEnable.value = enable
        settings.setPrivacyEnable(enable)
    }

    fun setReportTime(time: LocalTime) {
        _reportTime.value = time
        settings.setReportTime(time)
    }

    fun setEverydayReportsEnable(enable: Boolean) {
        _everydayReportEnable.value = enable
        settings.setEverydayReports(enable)
    }

    fun setNotificationMsg(msg: String) {
        _notificationMsg.value = msg
        settings.setNotificationMsg(msg)
    }

    fun setVoiceNotification(enable: Boolean) {
        _voiceNotification.value = enable
        settings.setVoiceNotification(enable)
    }
}