package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.customShape

@Composable
fun OutlineTextField(
    value: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    label: String? = null,
    shape: Shape = MaterialTheme.customShape.extraSmall,
    singleLine: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val animFontSize by animateFloatAsState(
        targetValue = if (!isFocused && value.isEmpty()) 16.sp.value else 14.sp.value,
        animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
    )

    OutlinedTextField(
        value = value,
        shape = shape,
        singleLine = singleLine,
        onValueChange = onValueChanged,
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
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
            if (isFocused) {
                IconButton(
                    onClick = {
                        if (value.isNotEmpty()) {
                            onValueChanged("")
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "cancel icon",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        placeholder = {
            Text(text = hint)
        },
        label = {
            label?.let {
                Text(
                    text = it,
                    fontSize = animFontSize.sp
                )
            }
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                isFocused = state.isFocused
            }
    )
}