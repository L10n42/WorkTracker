package com.kappdev.worktracker.tracker_feature

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.common.makeToast

class BiometricPromptHelper(
    private val activity: FragmentActivity
) {
    private val biometricsIgnoredErrors = listOf(
        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
        BiometricPrompt.ERROR_CANCELED,
        BiometricPrompt.ERROR_USER_CANCELED
    )

    private val promptInfo by lazy {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.biometric_prompt_title))
            .setSubtitle(activity.getString(R.string.biometric_prompt_description))
            .setNegativeButtonText(activity.getString(R.string.use_password_btn))
            .build()
    }

    fun showPrompt(onSuccess: () -> Unit) {
        val biometricPrompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode !in biometricsIgnoredErrors) {
                        activity.makeToast(errString)
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationFailed() {}
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}