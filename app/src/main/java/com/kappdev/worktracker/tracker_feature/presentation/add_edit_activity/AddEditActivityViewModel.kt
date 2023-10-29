package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.common.makeToast
import com.kappdev.worktracker.tracker_feature.domain.model.*
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivityByIdUseCase
import com.kappdev.worktracker.tracker_feature.domain.use_case.InsertActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val insertActivity: InsertActivityUseCase,
    private val getActivityById: GetActivityByIdUseCase,
    private val app: Application
) : ViewModel() {
    private val _activity = mutableStateOf(Activity())
    val activity: State<Activity> = _activity

    private val _target = mutableStateOf(Time())
    val target: State<Time> = _target

    private val _name = mutableStateOf("")
    val name: State<String> = _name

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
        targetInSec = target.value.inSeconds()
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
    }

    fun setTarget(time: Time) {
        _target.value = time
    }

    fun setName(value: String) {
        _name.value = value
    }
}