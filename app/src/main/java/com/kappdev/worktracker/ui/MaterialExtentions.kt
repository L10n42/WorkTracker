package com.kappdev.worktracker.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TextFieldDefaults.customOutlinedTextFieldColors(
    textColor: Color = MaterialTheme.colors.onSurface,
    disabledTextColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    cursorColor: Color = MaterialTheme.colors.primary,
    errorCursorColor: Color = MaterialTheme.colors.error,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onBackground,
    disabledBorderColor: Color = MaterialTheme.colors.onBackground,
    errorBorderColor: Color = MaterialTheme.colors.error,
    leadingIconColor: Color = MaterialTheme.colors.onSurface,
    disabledLeadingIconColor: Color = MaterialTheme.colors.onBackground,
    errorLeadingIconColor: Color = leadingIconColor,
    trailingIconColor: Color = MaterialTheme.colors.onSurface,
    disabledTrailingIconColor: Color = MaterialTheme.colors.onBackground,
    errorTrailingIconColor: Color = MaterialTheme.colors.error,
    focusedLabelColor: Color = MaterialTheme.colors.primary,
    unfocusedLabelColor: Color = MaterialTheme.colors.onSurface,
    disabledLabelColor: Color = MaterialTheme.colors.onBackground,
    errorLabelColor: Color = MaterialTheme.colors.error,
    placeholderColor: Color = MaterialTheme.colors.onBackground,
    disabledPlaceholderColor: Color = MaterialTheme.colors.onBackground
): TextFieldColors = outlinedTextFieldColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        errorBorderColor = errorBorderColor,
        disabledBorderColor = disabledBorderColor,
        leadingIconColor = leadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        trailingIconColor = trailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        backgroundColor = backgroundColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        placeholderColor = placeholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor
    )