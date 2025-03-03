package ru.mobileup.template.core.utils

import android.os.Build
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.view.WindowInsetsControllerCompat

private enum class NavBarPosition {
    Bottom, Left, Right, None
}

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
fun ConfigureSystemBars(
    statusBarColor: Color = Color.Unspecified,
    navigationBarColor: Color = Color.Unspecified,
    isStatusBarIconsDark: Boolean? = null,
    isNavigationBarIconsDark: Boolean? = null,
    isStatusBarContrastEnforced: Boolean? = null,
    isNavigationBarContrastEnforced: Boolean? = null,
) {
    val systemGestures = WindowInsets.systemGestures
    val navigationBars = WindowInsets.navigationBars
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val window = LocalActivity.current?.window
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

    Box(Modifier.fillMaxSize()) {
        if (statusBarColor != Color.Unspecified) {
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .drawBehind {
                        drawRect(statusBarColor)
                    }
            )
        }

        if (navigationBarColor != Color.Unspecified) {
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
                    drawRect(if (isGestureNavigation) Color.Transparent else navigationBarColor)
                }
            )
        }
    }

    if (window != null) {
        DisposableEffect(Unit) {
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            val defaultStatusBarAppearanceLight = insetsController.isAppearanceLightStatusBars
            val defaultNavBarAppearanceLight = insetsController.isAppearanceLightNavigationBars

            insetsController.apply {
                isStatusBarIconsDark?.let { isAppearanceLightStatusBars = it }
                isNavigationBarIconsDark?.let { isAppearanceLightNavigationBars = it }
            }

            onDispose {
                insetsController.apply {
                    isAppearanceLightStatusBars = defaultStatusBarAppearanceLight
                    isAppearanceLightNavigationBars = defaultNavBarAppearanceLight
                }
            }
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isStatusBarContrastEnforced?.let {
                DisposableEffect(Unit) {
                    val defaultStatusBarContrastEnforced = window.isStatusBarContrastEnforced
                    window.isStatusBarContrastEnforced = isStatusBarContrastEnforced
                    onDispose {
                        window.isStatusBarContrastEnforced = defaultStatusBarContrastEnforced
                    }
                }
            }
            isNavigationBarContrastEnforced?.let {
                DisposableEffect(Unit) {
                    val defaultNavBarContrastEnforced = window.isNavigationBarContrastEnforced
                    window.isNavigationBarContrastEnforced = isNavigationBarContrastEnforced
                    onDispose {
                        window.isNavigationBarContrastEnforced = defaultNavBarContrastEnforced
                    }
                }
            }
        } else {
            if (statusBarColor != Color.Unspecified) {
                DisposableEffect(Unit) {
                    val defaultStatusBarColor = window.statusBarColor
                    window.statusBarColor = AndroidColor.TRANSPARENT
                    onDispose {
                        window.statusBarColor = defaultStatusBarColor
                    }
                }
            }
            if (navigationBarColor != Color.Unspecified) {
                DisposableEffect(Unit) {
                    val defaultNavBarColor = window.navigationBarColor
                    window.navigationBarColor = AndroidColor.TRANSPARENT
                    onDispose {
                        window.navigationBarColor = defaultNavBarColor
                    }
                }
            }
        }
    }
}
