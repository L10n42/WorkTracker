package com.kappdev.worktracker.core.presentation.common_components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.customOutlinedTextFieldColors
import com.kappdev.worktracker.R

@Composable
fun PasswordTextField(
    value: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    isError: Boolean = false,
    imaAction: ImeAction = ImeAction.Done,
    label: String = stringResource(R.string.label_password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enable,
        isError = isError,
        singleLine = true,
        modifier = modifier,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.customOutlinedTextFieldColors(),
        label = {
            Text(
                text = label,
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = imaAction
        ),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation('*')
        },
        trailingIcon = {
            AnimatedVisibilityButton(isVisible = isPasswordVisible) {
                isPasswordVisible = !isPasswordVisible
            }
        }
    )
}

@Composable
private fun AnimatedVisibilityButton(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Crossfade(
            targetState = isVisible
        ) { visible ->
            Icon(
                imageVector = if (visible) {
                    Icons.Default.VisibilityOff
                } else {
                    Icons.Default.Visibility
                },
                contentDescription = "change password visibility button"
            )
        }
    }
}















