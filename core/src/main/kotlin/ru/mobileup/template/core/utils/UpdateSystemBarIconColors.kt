package ru.mobileup.template.core.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun UpdateSystemBarIconColorsIfNeeded(
    isLightTheme: Boolean,
    statusBarIconsColor: SystemBarIconsColor,
    navBarIconsColor: SystemBarIconsColor
) {
    val window = LocalActivity.current?.window
    window?.let {
        LaunchedEffect(statusBarIconsColor, navBarIconsColor, isLightTheme) {
            WindowInsetsControllerCompat(window, window.decorView).run {
                isAppearanceLightStatusBars = if (statusBarIconsColor.isSpecified) {
                    statusBarIconsColor == SystemBarIconsColor.Dark
                } else {
                    isLightTheme
                }

                isAppearanceLightNavigationBars = if (navBarIconsColor.isSpecified) {
                    navBarIconsColor == SystemBarIconsColor.Dark
                } else {
                    isLightTheme
                }
            }
        }
    }
}