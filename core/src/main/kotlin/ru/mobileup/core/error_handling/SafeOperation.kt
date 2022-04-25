package ru.mobileup.core.error_handling

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.aartikov.sesame.localizedstring.LocalizedString
import ru.mobileup.core.R

fun safeRun(
    errorHandler: ErrorHandler,
    showError: Boolean = true,
    block: () -> Unit
) {
    try {
        block()
    } catch (e: Exception) {
        errorHandler.handleError(e, showError)
    }
}

fun CoroutineScope.safeLaunch(
    errorHandler: ErrorHandler,
    showError: Boolean = true,
    onErrorHandled: ((e: Exception) -> Unit)? = null,
    block: suspend () -> Unit
): Job {
    return launch {
        try {
            block()
        } catch (e: CancellationException) {
            // do nothing
        } catch (e: Exception) {
            errorHandler.handleError(e, showError)
            onErrorHandled?.invoke(e)
        }
    }
}

fun CoroutineScope.safeLaunchRetryable(
    errorHandler: ErrorHandler,
    onErrorHandled: ((e: Exception) -> Unit)? = null,
    retryActionTitle: LocalizedString = LocalizedString.resource(R.string.common_retry),
    retryAction: () -> Unit,
    block: suspend () -> Unit
): Job {
    return launch {
        try {
            block()
        } catch (e: CancellationException) {
            // do nothing
        } catch (e: Exception) {
            errorHandler.handleErrorRetryable(
                throwable = e,
                retryActionTitle = retryActionTitle,
                retryAction = retryAction
            )
            onErrorHandled?.invoke(e)
        }
    }
}