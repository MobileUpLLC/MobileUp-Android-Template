package ru.mobileup.template.core.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.theme.custom.CustomTheme

@Composable
fun AppButton(
    buttonType: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    colors: ButtonColors = AppButtonDefaults.buttonColors(buttonType, isEnabled),
    border: BorderStroke = AppButtonDefaults.buttonBorder(buttonType, isEnabled),
    shape: Shape = AppButtonDefaults.buttonShape,
    contentPadding: PaddingValues = AppButtonDefaults.contentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = isEnabled,
        contentPadding = contentPadding,
        colors = colors,
        shape = shape,
        border = border,
        content = content
    )
}

@Composable
fun AppButton(
    text: String,
    buttonType: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    colors: ButtonColors = AppButtonDefaults.buttonColors(buttonType, isEnabled),
    border: BorderStroke = AppButtonDefaults.buttonBorder(buttonType, isEnabled),
    progressIndicatorColor: Color = AppButtonDefaults.progressIndicatorColor(buttonType),
    shape: Shape = AppButtonDefaults.buttonShape,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AppButton(
        modifier = modifier,
        buttonType = buttonType,
        onClick = onClick,
        interactionSource = interactionSource,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        colors = colors,
        shape = shape,
        border = border
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = progressIndicatorColor
            )
        } else {
            Text(
                text = text,
                color = AppButtonDefaults.buttonTextColor(buttonType, isEnabled),
                style = AppButtonDefaults.textStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(color = Color.Gray)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button Primary",
                buttonType = ButtonType.Primary,
                onClick = {},
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button Secondary",
                buttonType = ButtonType.Secondary,
                onClick = {},
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "",
                buttonType = ButtonType.Primary,
                onClick = {},
                isLoading = true,
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "",
                buttonType = ButtonType.Secondary,
                onClick = {},
                isLoading = true
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button Primary",
                buttonType = ButtonType.Primary,
                onClick = {},
                isEnabled = false
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button Secondary",
                buttonType = ButtonType.Secondary,
                onClick = {},
                isEnabled = false
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Primary,
                onClick = {},
                contentPadding = PaddingValues(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(
                    text = "Add",
                    style = CustomTheme.typography.button.bold,
                )
            }
        }
    }
}
