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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max

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
