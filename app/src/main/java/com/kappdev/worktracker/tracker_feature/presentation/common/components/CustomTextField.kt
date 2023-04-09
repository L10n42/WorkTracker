package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextField(
    value: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    cornerRadius: Dp = 8.dp,
    singleLine: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val animCorner by animateDpAsState(
        targetValue = if (isFocused) 0.dp else cornerRadius,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    val dynamicShape = RoundedCornerShape(
        topStart = cornerRadius, topEnd = cornerRadius, bottomStart = animCorner, bottomEnd = animCorner
    )

    TextField(
        value = value,
        singleLine = singleLine,
        shape = dynamicShape,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            unfocusedLabelColor = MaterialTheme.colors.onBackground,
            focusedLabelColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            unfocusedIndicatorColor = Color.Transparent
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
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        onValueChange = onValueChanged,
        placeholder = {
            Text(
                text = hint,
                color = MaterialTheme.colors.onBackground
            )
        },
        textStyle = TextStyle(fontSize = 18.sp),
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                isFocused = state.isFocused
            }
    )
}