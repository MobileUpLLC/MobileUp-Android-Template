package ru.mobileup.template.core.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mobileup.template.core.theme.custom.CustomTheme
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> BottomSheet(
    dialogControl: DialogControl<*, T>,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = true,
    onHideAnimationFinish: (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    val dismissableByUser by dialogControl.dismissableByUser.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val confirmValueChange = remember<(SheetValue) -> Boolean>(dialogControl) {
        { it != SheetValue.Hidden || dismissableByUser }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange
    )

    var delayedComponent by remember(dialogControl) {
        mutableStateOf(dialogControl.dialogSlot.value.child?.instance)
    }

    LaunchedEffect(Unit) {
        dialogControl
            .dialogSlot
            .onEach {
                val currentComponent = it.child?.instance
                delayedComponent = if (currentComponent != null) {
                    coroutineScope.launch { sheetState.show() }
                    currentComponent
                } else {
                    try {
                        sheetState.hide()
                    } catch (e: CancellationException) {
                        // do nothing
                    }
                    onHideAnimationFinish?.invoke()
                    null
                }
            }
            .launchIn(this)
    }

    val component = delayedComponent
    if (component != null) {
        ModalBottomSheet(
            modifier = modifier.statusBarsPadding(),
            onDismissRequest = {
                if (dismissableByUser) dialogControl.dismiss()
            },
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = dismissableByUser
            ),
            sheetState = sheetState,
            shape = shape,
            containerColor = CustomTheme.colors.background.screen,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null,
            content = {
                content(component)
            }
        )
    }
}
