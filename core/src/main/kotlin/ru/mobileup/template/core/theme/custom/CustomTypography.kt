package ru.mobileup.template.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class CustomTypography(
    val snackbar: TextStyle
)

val LocalCustomTypography = staticCompositionLocalOf<CustomTypography?> { null }
