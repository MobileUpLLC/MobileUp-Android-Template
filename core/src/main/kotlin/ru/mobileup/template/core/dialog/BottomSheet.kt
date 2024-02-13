package ru.mobileup.template.core.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import ru.mobileup.template.core.theme.custom.CustomTheme
import ru.mobileup.template.core.utils.navigationBarsWithImePaddingDp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> BottomSheet(
    dialogControl: DialogControl<*, T>,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = false,
    onHideAnimationFinished: (() -> Unit)? = null,
    content: @Composable ColumnScope.(T) -> Unit
) {
    val dismissableByUser by dialogControl.dismissableByUser.collectAsState()

    val confirmValueChange = remember<(SheetValue) -> Boolean>(dialogControl) {
        {
            when {
                it == SheetValue.Hidden && dismissableByUser -> {
                    dialogControl.dismiss()
                    true
                }

                it == SheetValue.Hidden && !dismissableByUser -> false

                else -> true
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange
    )

    val dialogSlot by dialogControl.dialogSlot.collectAsState()
    val currentComponent = dialogSlot.child?.instance
    var delayedComponent by remember(dialogControl) { mutableStateOf(currentComponent) }

    LaunchedEffect(currentComponent) {
        delayedComponent = if (currentComponent != null) {
            currentComponent
        } else {
            sheetState.hide() // suspends until animation finished
            onHideAnimationFinished?.invoke()
            null
        }
    }

    val component = delayedComponent
    if (component != null) {
        val navigationBarWithImePadding = navigationBarsWithImePaddingDp()
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                if (dismissableByUser) dialogControl.dismiss()
            },
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = dismissableByUser
            ),
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = CustomTheme.colors.background.screen,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            windowInsets = WindowInsets(0, 0, 0, 0),
            dragHandle = null,
            content = {
                Column(
                    Modifier.padding(bottom = navigationBarWithImePadding)
                ) {
                    content(component)
                }
            }
        )
    }
}