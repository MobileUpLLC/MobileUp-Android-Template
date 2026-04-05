package ru.mobileup.template.core.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowInsetsControllerCompat

@Composable
actual fun UpdateSystemBarIconColors(
    darkStatusBarIcons: Boolean,
    darkNavigationBarIcons: Boolean
) {
    val window = LocalActivity.current?.window
    window?.let {
        LaunchedEffect(darkStatusBarIcons, darkNavigationBarIcons) {
            WindowInsetsControllerCompat(window, window.decorView).run {
                isAppearanceLightStatusBars = darkStatusBarIcons
                isAppearanceLightNavigationBars = darkNavigationBarIcons
            }
        }
    }
}
