package ru.mobileup.template.core.dialog.standard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.template.core.theme.custom.CustomTheme

@Composable
fun StandardDialog(dialogControl: StandardDialogControl) {
    val dismissableByUser by dialogControl.dismissableByUser.collectAsState()
    val slot by dialogControl.dialogSlot.collectAsState()

    slot.child?.let {
        val data = it.instance

        AlertDialog(
            onDismissRequest = { dialogControl.dismiss() },
            properties = DialogProperties(
                dismissOnBackPress = dismissableByUser,
                dismissOnClickOutside = dismissableByUser
            ),
            shape = RoundedCornerShape(4.dp),

            title = {
                Text(
                    text = data.title.localized(),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        lineHeight = 20.sp
                    ),
                    color = CustomTheme.colors.text.primary
                )
            },

            text = if (data.message != null) {
                {
                    Text(
                        text = data.message.localized(),
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            lineHeight = 21.sp
                        ),
                        color = CustomTheme.colors.text.primary
                    )
                }
            } else {
                null
            },

            confirmButton = {
                DialogButton(
                    text = data.confirmButton.text.localized(),
                    onClick = data.confirmButton.action,
                    onDismiss = dialogControl::dismiss
                )
            },

            dismissButton = data.dismissButton?.let { dismissButton ->
                {
                    DialogButton(
                        text = dismissButton.text.localized(),
                        onClick = dismissButton.action,
                        onDismiss = dialogControl::dismiss
                    )
                }
            }
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp
        ),
        color = CustomTheme.colors.text.primary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable {
                onClick()
                onDismiss()
            }
            .padding(4.dp)
            .widthIn(min = 60.dp)
    )
}