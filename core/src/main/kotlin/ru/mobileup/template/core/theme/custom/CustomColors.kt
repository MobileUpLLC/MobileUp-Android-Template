package ru.mobileup.template.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf

data class CustomColors(
    val isLight: Boolean
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
