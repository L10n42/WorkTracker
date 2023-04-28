package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.customShape

@Composable
fun TargetField(
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = value.ifBlank { stringResource(R.string.target_placeholder) },
        shape = MaterialTheme.customShape.extraSmall,
        singleLine = true,
        readOnly = true,
        onValueChange = { /* NOTHING */ },
        textStyle = TextStyle(fontSize = 18.sp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            unfocusedLabelColor = MaterialTheme.colors.onSurface,
            focusedLabelColor = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.background,
            cursorColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.onBackground,
            focusedBorderColor = MaterialTheme.colors.primary,
            placeholderColor = MaterialTheme.colors.onBackground,
            trailingIconColor = MaterialTheme.colors.onBackground
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "drop down icon",
                modifier = Modifier.size(22.dp)
            )
        },
        label = {
            Text(
                text = stringResource(R.string.label_target),
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                if (state.isFocused) {
                    onClick()
                    focusManager.clearFocus()
                }
            }
    )
}