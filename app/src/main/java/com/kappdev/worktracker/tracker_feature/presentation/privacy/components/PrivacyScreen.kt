package com.kappdev.worktracker.tracker_feature.presentation.privacy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.common.makeToast
import com.kappdev.worktracker.core.presentation.common_components.PasswordTextField
import com.kappdev.worktracker.tracker_feature.BiometricPromptHelper
import com.kappdev.worktracker.tracker_feature.presentation.privacy.PrivacyViewModel
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun PrivacyScreen(
    biometricPromptHelper: BiometricPromptHelper,
    viewModel: PrivacyViewModel = hiltViewModel(),
    goNext: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        biometricPromptHelper.showPrompt(onSuccess = goNext)
    }

    val context = LocalContext.current
    val password = viewModel.password.value
    var isPasswordWrong by remember {
        mutableStateOf(false)
    }

    fun commitPassword() {
        val isPasswordRight = viewModel.checkPassword()
        if (isPasswordRight) {
            goNext()
        } else {
            isPasswordWrong = true
            context.makeToast(R.string.wrong_password_error)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        DecorationTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PasswordTextField(
                value = password,
                isError = isPasswordWrong,
                onValueChange = viewModel::setPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 32.dp),
                imaAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        commitPassword()
                    }
                )
            )

            UseBiometricPromptButton() {
                biometricPromptHelper.showPrompt(onSuccess = goNext)
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter)
                .padding(bottom = MaterialTheme.spacing.medium),
            shape = MaterialTheme.customShape.medium,
            onClick = ::commitPassword
        ) {
            Text(
                text = stringResource(R.string.btn_next),
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun UseBiometricPromptButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = "use_biometric_prompt button icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = stringResource(R.string.use_biometric_title),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun DecorationTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "login icon",
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(128.dp)
        )

        Text(
            text = stringResource(R.string.login_title),
            color = MaterialTheme.colors.onSurface,
            fontSize = 20.sp
        )
    }
}






























