package ru.mobileup.template.core.theme.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val isLight: Boolean
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
