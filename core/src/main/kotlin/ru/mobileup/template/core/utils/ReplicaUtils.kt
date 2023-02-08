package ru.mobileup.template.core.utils

import com.arkivanov.essenty.lifecycle.Lifecycle
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.aartikov.replica.decompose.coroutineScope
import me.aartikov.replica.decompose.observe
import me.aartikov.replica.keyed.KeyedReplica
import me.aartikov.replica.single.Loadable
import me.aartikov.replica.single.Replica
import me.aartikov.replica.single.currentState
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.error_handling.errorMessage

/**
 * An analogue of [Loadable] but with localized error message.
 */
data class LoadableState<T : Any>(
    val loading: Boolean = false,
    val data: T? = null,
    val error: StringDesc? = null
)

/**
 * Observes [Replica] and handles errors by [ErrorHandler].
 * @return Replica [Loadable] stateFlow
 */
fun <T : Any> Replica<T>.observe(
    lifecycle: Lifecycle,
    errorHandler: ErrorHandler
): StateFlow<LoadableState<T>> {

    val coroutineScope = lifecycle.coroutineScope()
    val observer = observe(lifecycle)

    observer
        .loadingErrorFlow
        .onEach { error ->
            errorHandler.handleError(
                error.exception,
                showError = observer.currentState.data != null // show error only if fullscreen error is not shown
            )
        }
        .launchIn(coroutineScope)

    val stateFlow = MutableStateFlow(observer.stateFlow.value.toLoadableState())
    observer
        .stateFlow
        .onEach {
            stateFlow.value = it.toLoadableState()
        }
        .launchIn(coroutineScope)

    return stateFlow
}

/**
 * Observes [KeyedReplica] and handles errors by [ErrorHandler].
 * @return Replica [Loadable] stateFlow
 */
fun <T : Any, K : Any> KeyedReplica<K, T>.observe(
    lifecycle: Lifecycle,
    errorHandler: ErrorHandler,
    key: StateFlow<K?>
): StateFlow<LoadableState<T>> {

    val coroutineScope = lifecycle.coroutineScope()

    val observer = observe(lifecycle, key)

    observer
        .loadingErrorFlow
        .onEach { error ->
            errorHandler.handleError(
                error.exception,
                showError = observer.currentState.data != null // show error only if fullscreen error is not shown
            )
        }
        .launchIn(coroutineScope)

    val stateFlow = MutableStateFlow(observer.stateFlow.value.toLoadableState())
    observer
        .stateFlow
        .onEach {
            stateFlow.value = it.toLoadableState()
        }
        .launchIn(coroutineScope)

    return stateFlow
}

fun <T : Any> Loadable<T>.toLoadableState(): LoadableState<T> {
    return LoadableState(
        loading = loading,
        data = data,
        error = error?.exception?.errorMessage
    )
}
