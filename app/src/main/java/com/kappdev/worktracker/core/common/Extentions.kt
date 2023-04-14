package com.kappdev.worktracker.core.common

import android.content.Context
import android.widget.Toast

fun Context.makeToast(resId: Int) {
    Toast.makeText(this, this.getText(resId), Toast.LENGTH_SHORT).show()
}