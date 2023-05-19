package com.kappdev.worktracker.tracker_feature.presentation.privacy

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    @Named("AppSettingsRep") private val settings: SettingsRepository
) : ViewModel() {

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    fun checkPassword() = settings.checkPassword(password.value)

    fun setPassword(password: String) {
        _password.value = password
    }
}