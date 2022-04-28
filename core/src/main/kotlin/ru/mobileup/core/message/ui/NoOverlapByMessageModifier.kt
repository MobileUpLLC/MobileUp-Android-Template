package ru.mobileup.core.message.ui

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned

val LocalMessageOffsets = staticCompositionLocalOf { mutableStateMapOf<Int, Int>() }

/**
 * This function allows you to specify that a message (see [MessageUi]) should be displayed above an UI element.
 */
fun Modifier.noOverlapByMessage(): Modifier = composed {
    val key = currentCompositeKeyHash
    val localMessageOffsets = LocalMessageOffsets.current

    DisposableEffect(Unit) {
        onDispose { localMessageOffsets.remove(key) }
    }
    then(
        onGloballyPositioned { layoutCoordinates ->
            if (layoutCoordinates.isAttached) {
                localMessageOffsets[key] = layoutCoordinates.size.height
            }
        }
    )
}