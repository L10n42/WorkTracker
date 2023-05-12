package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.customOutlinedTextFieldColors
import com.kappdev.worktracker.ui.customShape

@Composable
fun SelectorField(
    value: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    label: String = "",
    onClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = value,
        shape = MaterialTheme.customShape.extraSmall,
        enabled = enable,
        singleLine = true,
        readOnly = true,
        onValueChange = { /* NOTHING */ },
        textStyle = TextStyle(fontSize = 18.sp),
        colors = TextFieldDefaults.customOutlinedTextFieldColors(),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "drop down icon",
                modifier = Modifier.size(22.dp)
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                if (state.isFocused) {
                    onClick()
                }
            }
    )
}