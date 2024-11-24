package ru.mobileup.template.core.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.mobileup.template.core.theme.custom.CustomTheme

@Immutable
enum class ButtonType {
    Primary, Secondary
}

@Immutable
object AppButtonDefaults {

    @Stable
    val buttonShape = RoundedCornerShape(28.dp)

    @Stable
    val contentPadding = PaddingValues(16.dp)

    @Stable
    val textStyle: TextStyle
        @Composable
        get() = CustomTheme.typography.button.bold

    @Stable
    @Composable
    fun buttonColors(buttonType: ButtonType, isEnabled: Boolean): ButtonColors = ButtonColors(
        containerColor = buttonBackgroundColor(buttonType, isEnabled),
        contentColor = buttonTextColor(buttonType, isEnabled),
        disabledContainerColor = buttonBackgroundColor(buttonType, isEnabled),
        disabledContentColor = buttonTextColor(buttonType, isEnabled)
    )

    @Stable
    @Composable
    fun buttonBackgroundColor(
        buttonType: ButtonType,
        isEnabled: Boolean,
    ): Color = when (buttonType) {
        ButtonType.Primary -> CustomTheme.colors.button.primary
        ButtonType.Secondary -> CustomTheme.colors.button.secondary
    }.copy(alpha = if (isEnabled) 1f else 0.4f)

    @Stable
    @Composable
    fun buttonTextColor(buttonType: ButtonType, isEnabled: Boolean): Color = when (buttonType) {
        ButtonType.Primary -> CustomTheme.colors.text.invert
        ButtonType.Secondary -> CustomTheme.colors.text.primary
    }.copy(alpha = if (isEnabled) 1f else 0.4f)

    @Stable
    @Composable
    fun progressIndicatorColor(buttonType: ButtonType): Color = when (buttonType) {
        ButtonType.Primary -> CustomTheme.colors.text.invert
        ButtonType.Secondary -> CustomTheme.colors.text.primary
    }

    @Stable
    @Composable
    fun buttonBorder(buttonType: ButtonType, isEnabled: Boolean): BorderStroke = when (buttonType) {
        ButtonType.Secondary -> BorderStroke(
            1.dp,
            CustomTheme.colors.border.primary.copy(alpha = if (isEnabled) 1f else 0.4f)
        )

        else -> BorderStroke(0.dp, Color.Transparent)
    }
}
