package ru.mobileup.template.core.theme.custom

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

fun CustomColors.toMaterialColors(): ColorScheme {
    return if (isLight) {
        lightColorScheme()
    } else {
        darkColorScheme()
    }
}

fun CustomTypography.toMaterialTypography(): Typography {
    return Typography()
}
