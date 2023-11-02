package ru.mobileup.template.core.theme.values

import androidx.compose.ui.graphics.Color
import ru.mobileup.template.core.theme.custom.CustomColors
import ru.mobileup.template.core.theme.custom.SnackbarColors

val LightAppColors = CustomColors(
    snackbar = SnackbarColors(
        text = Color.White,
        background = Color.Black
    )
)

val DarkAppColors = CustomColors(
    snackbar = SnackbarColors(
        text = Color.Black,
        background = Color.White
    )
)
