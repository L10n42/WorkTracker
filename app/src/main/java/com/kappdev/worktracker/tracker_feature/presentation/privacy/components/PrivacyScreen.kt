package com.kappdev.worktracker.tracker_feature.presentation.privacy.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.presentation.common_components.PasswordTextField
import com.kappdev.worktracker.tracker_feature.presentation.privacy.PrivacyViewModel
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun PrivacyScreen(
    viewModel: PrivacyViewModel = hiltViewModel(),
    goNext: () -> Unit
) {
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
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {

        PasswordTextField(
            value = password,
            isError = isPasswordWrong,
            onValueChange = viewModel::setPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
                .padding(bottom = 32.dp),
            imaAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    commitPassword()
                }
            )
        )

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