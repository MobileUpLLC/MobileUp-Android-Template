package ru.mobileup.template.core.error_handling

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import me.aartikov.sesame.localizedstring.LocalizedString
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message
import timber.log.Timber

/**
 * Executes error processing: shows messages, log exceptions, notifies that auto logout happens.
 * Should be used only in Decompose components.
 */
class ErrorHandler(private val messageService: MessageService) {

    private val unauthorizedEventChannel = Channel<Unit>(Channel.UNLIMITED)

    val unauthorizedEventFlow = unauthorizedEventChannel.receiveAsFlow()

    // Used to not handle the same exception more than one time.
    private var lastHandledException: Exception? = null

    fun handleError(exception: Exception, showError: Boolean = true) {
        if (lastHandledException === exception) return
        lastHandledException = exception

        Timber.e(exception)
        when {
            exception is UnauthorizedException -> {
                unauthorizedEventChannel.trySend(Unit)
            }

            showError -> {
                messageService.showMessage(Message(text = exception.errorMessage))
            }
        }
    }

    /**
     * Allows to retry a failed action.
     */
    fun handleErrorRetryable(
        exception: Exception,
        retryActionTitle: LocalizedString,
        retryAction: () -> Unit
    ) {
        if (lastHandledException === exception) return
        lastHandledException = exception

        Timber.e(exception)
        when (exception) {
            is UnauthorizedException -> {
                unauthorizedEventChannel.trySend(Unit)
            }

            else -> {
                messageService.showMessage(
                    Message(
                        text = exception.errorMessage,
                        actionTitle = retryActionTitle,
                        action = retryAction
                    )
                )
            }
        }
    }
}