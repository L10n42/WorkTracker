package com.kappdev.worktracker.tracker_feature.presentation.settings

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.core.domain.util.Result
import com.kappdev.worktracker.core.domain.util.getMessageOrEmpty
import com.kappdev.worktracker.core.presentation.SnackbarState
import com.kappdev.worktracker.tracker_feature.domain.RemainderManager
import com.kappdev.worktracker.tracker_feature.domain.use_case.ImportDatabase
import com.kappdev.worktracker.tracker_feature.domain.use_case.ShareDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Named("AppSettingsRep") private val settings: SettingsRepository,
    private val remainderManager: RemainderManager,
    private val shareDatabase: ShareDatabase,
    private val importDatabase: ImportDatabase
) : ViewModel() {

    var isLoading = mutableStateOf(false)
        private set

    var showServiceInfo = mutableStateOf(false)
        private set

    private val _everydayReportEnable = mutableStateOf(settings.everydayReportsEnable())
    val everydayReportEnable: State<Boolean> = _everydayReportEnable

    private val _reportTime = mutableStateOf(settings.getReportTime())
    val reportTime: State<LocalTime> = _reportTime

    private val _showTimeTemplates = mutableStateOf(settings.isTimeTemplateEnabled())
    val showTimeTemplates: State<Boolean> = _showTimeTemplates

    private val _isThemeDark = mutableStateOf(settings.isThemeDark())
    val isThemeDark: State<Boolean> = _isThemeDark

    val snackbarState = SnackbarState()

    private var exportJob: Job? = null
    private var importJob: Job? = null

    fun exportDatabase() {
        exportJob?.cancel()
        exportJob = viewModelScope.launch(Dispatchers.IO) {
            loading {
                val result = shareDatabase()
                if (result is Result.Failure) {
                    snackbarState.show(result.getMessageOrEmpty())
                }
            }
        }
    }

    fun importDatabaseFrom(uri: Uri) {
        importJob?.cancel()
        importJob = viewModelScope.launch(Dispatchers.IO) {
            loading {
                val resultMessage = importDatabase(uri)
                snackbarState.show(resultMessage)
            }
        }
    }

    fun cancelRemainder() = remainderManager.stopRemainder()

    fun updateRemainder() {
        remainderManager.startRemainder(reportTime.value)
    }

    fun setDarkTheme(darkTheme: Boolean) {
        _isThemeDark.value = darkTheme
        settings.setDarkTheme(darkTheme)
    }

    fun enableTimeTemplates(enable: Boolean) {
        _showTimeTemplates.value = enable
        settings.enableTimeTemplate(enable)
    }

    fun setReportTime(time: LocalTime) {
        _reportTime.value = time
        settings.setReportTime(time)
    }

    fun setEverydayReportsEnable(enable: Boolean) {
        _everydayReportEnable.value = enable
        settings.setEverydayReports(enable)
    }

    fun showServiceInfo(show: Boolean) {
        showServiceInfo.value = show
    }

    private suspend fun loading(block: suspend () -> Unit) {
        isLoading.value = true
        block()
        isLoading.value = false
    }
}