package ru.mobileup.template.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val isLight: Boolean,
    val background: BackgroundColors,
    val text: TextColors,
    val icon: IconColors,
    val button: ButtonColors
)

data class BackgroundColors(
    val screen: Color,
    val toast: Color
)

data class TextColors(
    val primary: PrimaryColor
)

data class IconColors(
    val primary: PrimaryColor
)

data class ButtonColors(
    val primary: PrimaryColor
)

data class PrimaryColor(
    val default: Color,
    val pressed: Color = default,
    val disabled: Color = default,
    val invert: InvertColor = InvertColor(default = default)
)

data class InvertColor(
    val default: Color,
    val pressed: Color = default,
    val disabled: Color = default
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
