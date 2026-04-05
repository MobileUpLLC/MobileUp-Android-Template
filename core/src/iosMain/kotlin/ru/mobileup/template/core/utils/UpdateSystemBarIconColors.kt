package ru.mobileup.template.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
actual fun UpdateSystemBarIconColors(
    darkStatusBarIcons: Boolean,
    darkNavigationBarIcons: Boolean
) {
    val systemBarIconsColorHandler = LocalSystemBarIconsColorHandler.current

    LaunchedEffect(darkStatusBarIcons, darkNavigationBarIcons) {
        systemBarIconsColorHandler.updateSystemBarIconsColor(darkStatusBarIcons)
    }
}

interface SystemBarIconsColorHandler {
    fun updateSystemBarIconsColor(darkStatusBarIcons: Boolean)
}

val LocalSystemBarIconsColorHandler = staticCompositionLocalOf<SystemBarIconsColorHandler> {
    error("SystemBarIconsColorHandler is not present")
}
