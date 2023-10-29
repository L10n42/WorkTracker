package com.kappdev.worktracker.core.presentation

import android.content.Context
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SnackbarState(
    private val context: Context? = null
) {
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    suspend fun show(resId: Int) {
        requireNotNull(context) { "Context have to be paste to use this function" }
        val message = context.getString(resId)
        show(message)
    }

    suspend fun show(message: String) {
        _message.emit(message)
    }

    suspend fun clear() {
        _message.emit("")
    }
}