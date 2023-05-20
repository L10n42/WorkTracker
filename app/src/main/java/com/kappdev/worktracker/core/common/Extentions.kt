package com.kappdev.worktracker.core.common

import android.content.Context
import android.widget.Toast

fun Context.makeToast(resId: Int, length: Int = DEFAULT_LENGTH) {
    Toast.makeText(this, this.getText(resId), length).show()
}

fun Context.makeToast(msg: CharSequence, length: Int = DEFAULT_LENGTH) {
    Toast.makeText(this, msg, length).show()
}

private const val DEFAULT_LENGTH = Toast.LENGTH_SHORT