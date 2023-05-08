package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.domain.use_case.HighlightKeywordsTransformation
import com.kappdev.worktracker.tracker_feature.presentation.common.components.InfoButton
import com.kappdev.worktracker.ui.customShape

@Composable
fun NotificationMsgField(
    modifier: Modifier = Modifier,
    value: String,
    enable: Boolean = true,
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
        enabled = enable,
        singleLine = singleLine,
        onValueChange = onValueChanged,
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            unfocusedLabelColor = MaterialTheme.colors.onSurface,
            focusedLabelColor = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.background,
            cursorColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.onBackground,
            focusedBorderColor = MaterialTheme.colors.primary,
            placeholderColor = MaterialTheme.colors.onBackground,
            trailingIconColor = MaterialTheme.colors.onSurface,
            disabledTrailingIconColor = MaterialTheme.colors.onBackground
        ),
        visualTransformation = HighlightKeywordsTransformation(
            keywords = CountdownService.CountdownKeywords,
            style = SpanStyle(
                color = if (enable) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                fontStyle = FontStyle.Italic
            )
        ),
        trailingIcon = {
            InfoButton(
                text = "Use @name to mention the activity, and use @time to mention the time."
            )
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