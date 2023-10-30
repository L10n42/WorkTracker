package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.common.makeToast
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.domain.model.*
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivityByIdUseCase
import com.kappdev.worktracker.tracker_feature.domain.use_case.InsertActivityUseCase
import com.kappdev.worktracker.ui.theme.ActivityColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val insertActivity: InsertActivityUseCase,
    private val getActivityById: GetActivityByIdUseCase,
    @Named("AppSettingsRep") private val settings: SettingsRepository,
    private val app: Application
) : ViewModel() {

    val showTimeTemplateByDefault = settings.isTimeTemplateEnabled()

    private val _activity = mutableStateOf(Activity())
    val activity: State<Activity> = _activity

    private val _target = mutableStateOf(Time())
    val target: State<Time> = _target

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _color = mutableStateOf(ActivityColors.random())
    val color: State<Color> = _color

    fun detectErrorAndShowToast() {
        when {
            name.value.isBlank() -> app.makeToast(R.string.unfilled_field_error)
            target.value.isEmpty() -> app.makeToast(R.string.unfilled_target_error)
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            insertActivity(packActivity())
        }
    }

    private fun packActivity() = Activity(
        id = activity.value.id,
        name = name.value.trim(),
        creationTimestamp = getTimestamp(),
        targetInSec = target.value.inSeconds(),
        color = color.value.toArgb()
    )

    private fun getTimestamp(): Long {
        return if (activity.value.id > 0) activity.value.creationTimestamp else System.currentTimeMillis()
    }

    fun canSave() = name.value.trim().isNotEmpty() && target.value.isNotEmpty()

    fun getActivityBy(id: Long, onError: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getActivityById(id)?.let { fillWith(it) } ?: onError()
        }
    }

    private fun fillWith(activity: Activity) {
        _activity.value = activity
        setName(activity.name)
        setTarget(Time.from(activity.targetInSec))
        setColor(activity.getColor())
    }

    fun setColor(color: Color) {
        _color.value = color
    }

    fun setTarget(time: Time) {
        _target.value = time
    }

    fun setName(value: String) {
        _name.value = value
    }
}