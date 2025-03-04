package ru.mobileup.template.core.utils

import android.os.Build
import android.view.Window
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsStartWidth
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color as AndroidColor

private enum class NavBarPosition {
    Bottom, Left, Right, None
}

private val DefaultLightScrim = AndroidColor.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DefaultDarkScrim = AndroidColor.argb(0x80, 0x1b, 0x1b, 0x1b)

/**
 * Configures system bar colors and contrast enforcement while adapting to different navigation modes.
 *
 * This function dynamically adjusts the system bars (status bar and navigation bar) based on the current navigation mode,
 * ensuring correct background colors and enforcing contrast settings when necessary.
 *
 * Supports screen rotation and correctly handles the navigation bar in both portrait and landscape orientations.
 *
 * **Usage:**
 * Call this function **after** the main content, not before it.
 *
 * ✅ **Correct Usage:**
 * ```
 * @Composable
 * fun Content() {
 *     Scaffold { ... }
 *     ConfigureSystemBars()
 * }
 * ```
 * ❌ **Incorrect Usage:**
 * ```
 * @Composable
 * fun Content() {
 *     ConfigureSystemBars()
 *     Scaffold { ... }
 * }
 * ```
 */
@Suppress("ModifierMissing")
@Composable
fun ConfigureSystemBars(settings: SystemBarsSettings) {
    val systemGestures = WindowInsets.systemGestures
    val navigationBars = WindowInsets.navigationBars
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val window = LocalActivity.current?.window
    val isDarkTheme = isSystemInDarkTheme()
    val isGestureNavigation by remember {
        derivedStateOf {
            systemGestures.run {
                getLeft(density, layoutDirection) > 0 && getRight(density, layoutDirection) > 0
            }
        }
    }
    val navBarPosition by remember {
        derivedStateOf {
            when {
                navigationBars.getBottom(density) > 0 -> NavBarPosition.Bottom
                navigationBars.getLeft(density, layoutDirection) > 0 -> NavBarPosition.Left
                navigationBars.getRight(density, layoutDirection) > 0 -> NavBarPosition.Right
                else -> NavBarPosition.None
            }
        }
    }

    window?.let {
        LaunchedEffect(settings) {
            updateSystemBarIconColors(
                window = window,
                isStatusBarIconsDark = settings.isStatusBarIconsDark,
                isNavigationBarIconsDark = settings.isNavigationBarIconsDark
            )
            updateNavigationBarColor(
                window = window,
                navigationBarColor = settings.navigationBarColor,
                isNavigationBarContrastEnforced = settings.isNavigationBarContrastEnforced,
                isDarkTheme = isDarkTheme
            )
        }
    }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .drawBehind {
                    drawRect(settings.statusBarColor)
                }
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val navBarModifier = remember(navBarPosition) {
                when (navBarPosition) {
                    NavBarPosition.Bottom -> Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .windowInsetsBottomHeight(navigationBars)

                    NavBarPosition.Left -> Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .windowInsetsStartWidth(navigationBars)

                    NavBarPosition.Right -> Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .windowInsetsEndWidth(navigationBars)

                    NavBarPosition.None -> Modifier
                }
            }

            Box(
                navBarModifier.drawBehind {
                    drawRect(
                        if (isGestureNavigation) Color.Transparent else settings.navigationBarColor
                    )
                }
            )
        }
    }
}

private fun updateSystemBarIconColors(
    window: Window,
    isStatusBarIconsDark: Boolean,
    isNavigationBarIconsDark: Boolean,
) {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        isAppearanceLightStatusBars = isStatusBarIconsDark
        isAppearanceLightNavigationBars = isNavigationBarIconsDark
    }
}

@Suppress("DEPRECATION")
private fun updateNavigationBarColor(
    window: Window,
    navigationBarColor: Color,
    isNavigationBarContrastEnforced: Boolean,
    isDarkTheme: Boolean,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = isNavigationBarContrastEnforced
    } else {
        window.navigationBarColor = if (isNavigationBarContrastEnforced) {
            if (navigationBarColor != Color.Transparent) {
                navigationBarColor.toArgb()
            } else {
                if (isDarkTheme) DefaultDarkScrim else DefaultLightScrim
            }
        } else {
            navigationBarColor.toArgb()
        }
    }
}
