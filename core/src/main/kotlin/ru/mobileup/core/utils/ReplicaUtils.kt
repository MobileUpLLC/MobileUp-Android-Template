package ru.mobileup.core.utils

import androidx.compose.runtime.State
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.decompose.observe
import me.aartikov.replica.single.Loadable
import me.aartikov.replica.single.Replica
import ru.mobileup.core.error_handling.ErrorHandler

fun <T : Any> Replica<T>.observe(
    lifecycle: Lifecycle,
    errorHandler: ErrorHandler
): State<Loadable<T>> {
    return observe(
        lifecycle,
        onError = { error, state ->
            errorHandler.handleError(
                throwable = error.exception,
                showError = state.data != null // show error only if fullscreen error is not shown
            )
        }
    )
}