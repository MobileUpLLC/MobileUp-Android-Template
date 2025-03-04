package ru.mobileup.template.core.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max

val LocalSystemBarsSettings = staticCompositionLocalOf {
    mutableStateListOf<SystemBarsSettings>()
}

data class SystemBarsSettings(
    val statusBarColor: Color = Color.Transparent,
    val navigationBarColor: Color = Color.Transparent,
    val isStatusBarIconsDark: Boolean = true,
    val isNavigationBarIconsDark: Boolean = true,
    val isNavigationBarContrastEnforced: Boolean = true,
)

fun List<SystemBarsSettings>.accumulate(): SystemBarsSettings =
    if (isEmpty()) SystemBarsSettings() else last()

@Composable
fun SystemBars(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    isStatusBarIconsDark: Boolean = true,
    isNavigationBarIconsDark: Boolean = true,
    isNavigationBarContrastEnforced: Boolean = true,
) {
    val newSystemBarsSettings = SystemBarsSettings(
        statusBarColor = statusBarColor,
        navigationBarColor = navigationBarColor,
        isStatusBarIconsDark = isStatusBarIconsDark,
        isNavigationBarIconsDark = isNavigationBarIconsDark,
        isNavigationBarContrastEnforced = isNavigationBarContrastEnforced
    )
    val systemBarsSettings = LocalSystemBarsSettings.current

    DisposableEffect(systemBarsSettings) {
        systemBarsSettings.add(newSystemBarsSettings)
        onDispose { systemBarsSettings.removeAt(systemBarsSettings.lastIndex) }
    }
}

fun Modifier.systemBarsWithImePadding() = systemBarsPadding().imePadding()

fun Modifier.navigationBarsWithImePadding() = navigationBarsPadding().imePadding()

val statusBarsPaddingDp: Dp
    @Composable
    get() = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

val navigationBarsPaddingDp: Dp
    @Composable
    get() = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

val imePaddingDp: Dp
    @Composable
    get() = WindowInsets.ime.asPaddingValues().calculateBottomPadding()

val navigationBarsWithImePaddingDp: Dp
    @Composable
    get() = max(navigationBarsPaddingDp, imePaddingDp)
