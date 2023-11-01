package com.kappdev.worktracker.tracker_feature.presentation.common.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.theme.getButtonColor

@Composable
fun ServiceInfoSheet(
    onGotIt: () -> Unit
) {
    val context = LocalContext.current

    val openAppSettings = {
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingsIntent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(settingsIntent)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.attention),
            color = MaterialTheme.colors.onSurface,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = styleMessage(message),
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SheetButton(
                title = stringResource(R.string.btn_settings),
                titleColor = MaterialTheme.colors.onSurface,
                onClick = openAppSettings
            )

            SheetButton(
                title = stringResource(R.string.got_it),
                titleColor = MaterialTheme.colors.primary,
                onClick = onGotIt
            )
        }
    }
}

@Composable
private fun SheetButton(
    title: String,
    titleColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .width(150.dp)
            .height(42.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = getButtonColor()
        )
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = titleColor
        )
    }
}

private val message by lazy {
    """
        We hope you're enjoying using our app! However, on some devices, the tracking service may stop due to battery optimization.

        If you faced with this problem the following steps may help you:

        • Look for *Battery* or *Battery Optimization* and set our app to *Not optimized* or *Allow background activity*.

        • In your device's settings, search for *App Restrictions* or *Background Activity Restrictions* and ensure that our app is allowed to run in the background.

        Please note that these steps may vary depending on your device's make and model.

        We apologize for any inconvenience you've experienced and appreciate your understanding.

        Thank you for choosing the *WorkTracker*!
    """.trimIndent()
}

@Composable
private fun styleMessage(input: String) = buildAnnotatedString {
    val regex = "\\*(.*?)\\*".toRegex()
    val matches = regex.findAll(input)
    var lastIndex = 0

    for (match in matches) {
        append(input.substring(lastIndex, match.range.first))

        val boldText = match.groupValues[1]

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(boldText)
        }

        lastIndex = match.range.last + 1
    }

    if (lastIndex < input.length) {
        append(input.substring(lastIndex, input.length))
    }
}
