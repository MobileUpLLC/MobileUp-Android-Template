package ru.mobileup.template.core.theme.values

import androidx.compose.ui.graphics.Color
import ru.mobileup.template.core.theme.custom.BackgroundColors
import ru.mobileup.template.core.theme.custom.ButtonColors
import ru.mobileup.template.core.theme.custom.CustomColors
import ru.mobileup.template.core.theme.custom.IconColors
import ru.mobileup.template.core.theme.custom.InvertColor
import ru.mobileup.template.core.theme.custom.PrimaryColor
import ru.mobileup.template.core.theme.custom.TextColors

val LightAppColors = CustomColors(
    background = BackgroundColors(
        screen = Color.White,
        toast = Color.Black
    ),
    text = TextColors(
        primary = PrimaryColor(
            default = Color.Black,
            invert = InvertColor(default = Color.White)
        )
    ),
    icon = IconColors(
        primary = PrimaryColor(
            default = Color.Black,
            invert = InvertColor(default = Color.White)
        )
    ),
    button = ButtonColors(
        primary = PrimaryColor(
            default = Color(red = 103, green = 80, blue = 164)
        )
    )
)

val DarkAppColors = LightAppColors
