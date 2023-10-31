package com.kappdev.worktracker.tracker_feature.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetSortedActivities
import com.kappdev.worktracker.tracker_feature.domain.use_case.RemoveActivitiesUseCase
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @Named("AppSettingsRep") private val settings: SettingsRepository,
    private val getSortedActivities: GetSortedActivities,
    private val removeActivities: RemoveActivitiesUseCase,
    val stopwatchController: StopwatchController,
    val countdownController: CountdownController
) : ViewModel() {

    val showTimeTemplateByDefault = settings.isTimeTemplateEnabled()

    val selectedActivities = mutableStateListOf<Activity>()

    private val _screenState = mutableStateOf(MainScreenState.NORMAL_MODE)
    val screenState: State<MainScreenState> = _screenState

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _dialog = mutableStateOf<MainScreenDialog?>(null)
    val dialog: State<MainScreenDialog?> = _dialog

    private val _activities = mutableStateOf<List<Activity>>(emptyList())
    val activities: State<List<Activity>> = _activities

    private val _order = mutableStateOf(settings.getActivityOrder())
    val order: State<ActivityOrder> = _order

    fun launch() {
        getAllActivities()
    }

    fun removeSelected(then: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            removeActivities(selectedActivities)
            refreshData()
            then()
        }
    }

    fun refreshData() = getAllActivities()
    private fun getAllActivities() {
        viewModelScope.launch(Dispatchers.IO) {
            setDataState(DataState.LOADING)
            val activities = getSortedActivities(order.value)
            _activities.value = activities
            if (activities.isEmpty()) setDataState(DataState.NO_DATA) else setDataState(DataState.READY)
        }
    }

    fun select(activity: Activity) { selectedActivities.add(activity) }
    fun deselect(activity: Activity) { selectedActivities.remove(activity) }
    fun cancelSelection() {
        deselectAll()
        switchSelectionModeOff()
    }
    fun deselectAll() { selectedActivities.clear() }
    fun selectAll() {
        selectedActivities.clear()
        selectedActivities.addAll(activities.value)
    }

    fun setOrder(order: ActivityOrder) {
        settings.setActivityOrder(order)
        _order.value = order
    }

    fun allSelected() = selectedActivities.containsAll(activities.value)

    private fun setDataState(state: DataState) { _dataState.value = state }

    fun switchSelectionModeOn() { _screenState.value = MainScreenState.SELECTION_MODE }
    private fun switchSelectionModeOff() { _screenState.value = MainScreenState.NORMAL_MODE }

    fun openDialog(dialog: MainScreenDialog) { _dialog.value = dialog }
    fun closeDialog() { _dialog.value = null }
}