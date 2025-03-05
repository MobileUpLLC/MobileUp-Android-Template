package ru.mobileup.template.core.utils

import android.os.Build
import android.view.Window
import androidx.activity.compose.LocalActivity
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.util.lerp
import androidx.core.view.WindowInsetsControllerCompat
import ru.mobileup.template.core.theme.custom.CustomTheme
import android.graphics.Color as AndroidColor

private enum class NavBarPosition {
    Bottom, Left, Right, None
}

/**
 * Configures system bar colors while adapting to different navigation modes.
 *
 * This function dynamically adjusts the system bars (status bar and navigation bar)
 * based on the current navigation mode, ensuring correct background colors.
 *
 * Supports screen rotation and correctly handles the navigation bar in both portrait and landscape orientations.
 *
 * **Usage:**
 * Call this function **after** the main content, not before it.
 */
@Suppress("ModifierMissing")
@Composable
fun ConfigureSystemBars(settings: SystemBarsSettings) {
    val systemGestures = WindowInsets.systemGestures
    val navigationBars = WindowInsets.navigationBars
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val window = LocalActivity.current?.window
    val backgroundColor = CustomTheme.colors.background.screen
    val isLightTheme = CustomTheme.colors.isLight
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
    val defaultSystemBarColor = remember(backgroundColor) {
        val luminance = backgroundColor.luminance()
        val alpha = lerp(start = 0.5f, stop = 0.9f, fraction = luminance)
        backgroundColor.copy(alpha = alpha)
    }
    val statusBarColor = remember(settings, defaultSystemBarColor) {
        settings.statusBarColor.takeOrElse { defaultSystemBarColor }
    }
    val navBarColor = remember(settings, defaultSystemBarColor, isGestureNavigation) {
        if (isGestureNavigation) {
            Color.Transparent
        } else {
            settings.navigationBarColor.takeOrElse { defaultSystemBarColor }
        }
    }

    window?.let {
        LaunchedEffect(Unit) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            } else {
                window.navigationBarColor = AndroidColor.TRANSPARENT
                window.statusBarColor = AndroidColor.TRANSPARENT
            }
        }

        LaunchedEffect(settings, isLightTheme) {
            updateSystemBarIconColors(
                window = window,
                isLightTheme = isLightTheme,
                statusBarIconsColor = settings.statusBarIconsColor,
                navBarIconsColor = settings.navigationBarIconsColor
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
                    drawRect(statusBarColor)
                }
        )

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
                drawRect(navBarColor)
            }
        )
    }
}

private fun updateSystemBarIconColors(
    window: Window,
    isLightTheme: Boolean,
    statusBarIconsColor: SystemBarIconsColor,
    navBarIconsColor: SystemBarIconsColor,
) = WindowInsetsControllerCompat(window, window.decorView).run {
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
