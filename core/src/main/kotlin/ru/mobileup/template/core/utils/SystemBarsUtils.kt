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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max

val LocalSystemBarsSettings = staticCompositionLocalOf {
    mutableStateListOf<SystemBarsSettings>()
}

enum class SystemBarIconsColor {
    Light, Dark, Unspecified;

    @Stable
    inline val isSpecified: Boolean get() = this != Unspecified

    inline fun takeOrElse(block: () -> SystemBarIconsColor): SystemBarIconsColor =
        if (isSpecified) this else block()
}

/**
 * Represents the configuration settings for system bars, including status bar and navigation bar colors,
 * as well as icon colors for each.
 *
 * @property statusBarColor The color of the status bar. If set to [Color.Unspecified], the primary background
 * color from the theme is used, and an alpha value is applied dynamically based on its luminance.
 * @property navigationBarColor The color of the navigation bar. If set to [Color.Unspecified], the primary
 * background color from the theme is used, and an alpha value is applied dynamically based on its luminance.
 * @property statusBarIconsColor The color scheme for the status bar icons. If set to [SystemBarIconsColor.Unspecified],
 * the value is determined based on the app's theme: dark icons for light themes, light icons for dark themes.
 * @property navigationBarIconsColor The color scheme for the navigation bar icons. If set to [SystemBarIconsColor.Unspecified],
 * the value is determined based on the app's theme: dark icons for light themes, light icons for dark themes.
 */
data class SystemBarsSettings(
    val statusBarColor: Color,
    val navigationBarColor: Color,
    val statusBarIconsColor: SystemBarIconsColor,
    val navigationBarIconsColor: SystemBarIconsColor,
) {
    companion object {
        val DEFAULT = SystemBarsSettings(
            statusBarColor = Color.Unspecified,
            navigationBarColor = Color.Unspecified,
            statusBarIconsColor = SystemBarIconsColor.Unspecified,
            navigationBarIconsColor = SystemBarIconsColor.Unspecified,
        )
    }
}

fun List<SystemBarsSettings>.accumulate() = fold(SystemBarsSettings.DEFAULT) { acc, settings ->
    SystemBarsSettings(
        statusBarColor = settings.statusBarColor.takeOrElse(acc::statusBarColor),
        navigationBarColor = settings.navigationBarColor.takeOrElse(acc::navigationBarColor),
        statusBarIconsColor = settings.statusBarIconsColor.takeOrElse(acc::statusBarIconsColor),
        navigationBarIconsColor = settings.navigationBarIconsColor.takeOrElse(acc::navigationBarIconsColor),
    )
}

@Composable
fun SystemBars(
    statusBarColor: Color = Color.Unspecified,
    navigationBarColor: Color = Color.Unspecified,
    statusBarIconsColor: SystemBarIconsColor = SystemBarIconsColor.Unspecified,
    navigationBarIconsColor: SystemBarIconsColor = SystemBarIconsColor.Unspecified,
) {
    val newSystemBarsSettings = SystemBarsSettings(
        statusBarColor = statusBarColor,
        navigationBarColor = navigationBarColor,
        statusBarIconsColor = statusBarIconsColor,
        navigationBarIconsColor = navigationBarIconsColor,
    )
    val systemBarsSettings = LocalSystemBarsSettings.current

    DisposableEffect(newSystemBarsSettings) {
        systemBarsSettings.add(newSystemBarsSettings)
        onDispose { systemBarsSettings.remove(newSystemBarsSettings) }
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
