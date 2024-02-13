package ru.mobileup.template.core.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import java.util.UUID

val LocalSystemBarsSettings =
    staticCompositionLocalOf { mutableStateMapOf<String, SystemBarsSettings>() }

data class SystemBarsSettings(
    val transparentNavigationBar: Boolean,
    val lightStatusBarIcons: Boolean
)

@Composable
fun SystemBars(
    transparentNavigationBar: Boolean = false,
    lightStatusBarIcons: Boolean = false
) {
    val key = remember { UUID.randomUUID().toString() }
    val systemBarsSettings = SystemBarsSettings(transparentNavigationBar, lightStatusBarIcons)
    val systemBarsSettingsMap = LocalSystemBarsSettings.current

    DisposableEffect(systemBarsSettings) {
        systemBarsSettingsMap[key] = systemBarsSettings
        onDispose { systemBarsSettingsMap.remove(key) }
    }
}

@Composable
fun Map<String, SystemBarsSettings>.accumulate(): SystemBarsSettings {
    return SystemBarsSettings(
        transparentNavigationBar = values.any { it.transparentNavigationBar },
        lightStatusBarIcons = values.any { it.lightStatusBarIcons }
    )
}

fun Modifier.systemBarsWithImePadding() = systemBarsPadding().imePadding()

fun Modifier.navigationBarsWithImePadding() = navigationBarsPadding().imePadding()

@Composable
fun statusBarsPaddingDp(): Dp {
    return WindowInsets.statusBars.only(WindowInsetsSides.Top)
        .asPaddingValues().calculateTopPadding()
}

@Composable
fun navigationBarsPaddingDp(): Dp {
    return WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        .asPaddingValues().calculateBottomPadding()
}

@Composable
fun navigationBarsWithImePaddingDp(): Dp {
    val navigationBarPaddingDp = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        .asPaddingValues().calculateBottomPadding()

    val imePaddingDp = WindowInsets.ime.only(WindowInsetsSides.Bottom)
        .asPaddingValues().calculateBottomPadding()

    return max(navigationBarPaddingDp, imePaddingDp)
}