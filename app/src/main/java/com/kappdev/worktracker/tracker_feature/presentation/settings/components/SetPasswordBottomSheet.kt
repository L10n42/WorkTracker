package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.presentation.common_components.PasswordTextField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.settings.PasswordState
import com.kappdev.worktracker.tracker_feature.presentation.settings.isNotBlank
import com.kappdev.worktracker.tracker_feature.presentation.settings.isSuitableLength
import com.kappdev.worktracker.ui.spacing

@Composable
fun SetPasswordBottomSheet(
    password: PasswordState,
    onPasswordChanged: (PasswordState) -> Unit,
    onCommit: () -> Boolean
) {
    val focusManager = LocalFocusManager.current
    var isPasswordError by remember { mutableStateOf(false) }

    fun updatePassword(first: String = password.first, second: String = password.second) {
        onPasswordChanged(
            PasswordState(first, second)
        )
    }

    fun commitPassword() {
        val isPasswordCorrect = onCommit()
        if (!isPasswordCorrect) {
            isPasswordError = true
        }
    }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
            .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.set_password_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )

        PasswordTextField(
            value = password.first,
            isError = isPasswordError,
            modifier = Modifier.fillMaxWidth(),
            imaAction = ImeAction.Next,
            onValueChange = { newPass ->
                updatePassword(first = newPass)
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            )
        )

        PasswordTextField(
            value = password.second,
            isError = isPasswordError,
            modifier = Modifier.fillMaxWidth(),
            imaAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (password.isNotBlank() && password.isSuitableLength()) {
                        commitPassword()
                    }
                }
            ),
            onValueChange = { newPass ->
                updatePassword(second = newPass)
            }
        )

        VerticalSpace(MaterialTheme.spacing.small)

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = CircleShape,
            enabled = password.isNotBlank() && password.isSuitableLength(),
            onClick = ::commitPassword
        ) {
            Text(
                text = stringResource(R.string.btn_ok),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}