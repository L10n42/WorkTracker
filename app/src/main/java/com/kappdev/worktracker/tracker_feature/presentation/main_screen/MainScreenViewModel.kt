package com.kappdev.worktracker.tracker_feature.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivitiesUseCase
import com.kappdev.worktracker.tracker_feature.domain.use_case.RemoveActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getActivities: GetActivitiesUseCase,
    private val removeActivities: RemoveActivitiesUseCase,
    val stopwatchController: StopwatchController,
    val countdownController: CountdownController
) : ViewModel() {

    val selectedActivities = mutableStateListOf<Activity>()

    private val _screenState = mutableStateOf(MainScreenState.NORMAL_MODE)
    val screenState: State<MainScreenState> = _screenState

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _dialog = mutableStateOf<MainScreenDialog?>(null)
    val dialog: State<MainScreenDialog?> = _dialog

    private val _activities = mutableStateOf<List<Activity>>(emptyList())
    val activities: State<List<Activity>> = _activities

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    private val _bottomSheet = mutableStateOf<MainScreenBottomSheet?>(null)
    val bottomSheet: State<MainScreenBottomSheet?> = _bottomSheet

    private var activityJob: Job? = null

    init {
        getAllActivities()
    }

    fun removeSelected(then: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            removeActivities(selectedActivities)
            then()
        }
    }

    private fun getAllActivities() {
        activityJob?.cancel()
        activityJob = getActivities().onEach { activities ->
            setDataState(DataState.LOADING)
            _activities.value = activities
            if (activities.isEmpty()) setDataState(DataState.NO_DATA) else setDataState(DataState.READY)
        }.launchIn(viewModelScope)
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

    fun allSelected() = selectedActivities.containsAll(activities.value)

    private fun setDataState(state: DataState) { _dataState.value = state }

    fun setScreenState(state: MainScreenState) { _screenState.value = state }
    fun switchSelectionModeOn() { _screenState.value = MainScreenState.SELECTION_MODE }
    fun switchSelectionModeOff() { _screenState.value = MainScreenState.NORMAL_MODE }

    fun clearSheet() = closeSheet()
    fun closeSheet() { _bottomSheet.value = null }
    fun openSheet(sheet: MainScreenBottomSheet) { _bottomSheet.value = sheet }

    fun openDialog(dialog: MainScreenDialog) { _dialog.value = dialog }
    fun closeDialog() { _dialog.value = null }

    fun openActivity(activity: Activity) { /* TODO(make navigation to activity review screen) */ }
    fun navigate(route: String) { _navigate.value = route }
    fun clearNavigationRoute() { _navigate.value = null }
}