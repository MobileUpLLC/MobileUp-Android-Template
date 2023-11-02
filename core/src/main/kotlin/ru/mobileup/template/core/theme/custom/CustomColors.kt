package ru.mobileup.template.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val snackbar: SnackbarColors
)

data class SnackbarColors(
    val text: Color,
    val background: Color,
    val icon: Color = text
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
