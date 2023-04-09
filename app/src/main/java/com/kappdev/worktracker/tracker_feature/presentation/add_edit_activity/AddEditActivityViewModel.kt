package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.use_case.InsertActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val insertActivity: InsertActivityUseCase,
    private val app: Application
) : ViewModel() {
    var activity = Activity.Empty
        private set

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            insertActivity(packActivity())
        }
    }

    private fun packActivity() = Activity(
        id = activity.id,
        name = name.value,
        creationTimestamp = getTimestamp()
    )

    private fun getTimestamp(): Long {
        return if (activity.id > 0) activity.creationTimestamp else System.currentTimeMillis()
    }

    fun canSave() = name.value.trim().isNotEmpty()

    fun showError(resId: Int) {
        _error.value = app.getString(resId)
    }

    fun hideError() {
        _error.value = null
    }

    fun navigate(route: String) {
        _navigate.value = route
    }

    fun clearNavigationRoute() {
        _navigate.value = null
    }

    fun setName(value: String) {
        _name.value = value
    }
}