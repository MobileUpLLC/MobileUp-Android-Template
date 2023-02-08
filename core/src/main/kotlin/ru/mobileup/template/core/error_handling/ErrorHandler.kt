package ru.mobileup.template.core.error_handling

import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import me.aartikov.sesame.localizedstring.LocalizedString
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message
import ru.mobileup.template.core.utils.e

/**
 * Executes error processing: shows error messages, logs exceptions, notifies that auto logout happens.
 * Should be used only in Decompose components.
 */
class ErrorHandler(private val messageService: MessageService) {

    private val logger = Logger.withTag("ErrorHandler")

    private val unauthorizedEventChannel = Channel<Unit>(Channel.UNLIMITED)

    val unauthorizedEventFlow = unauthorizedEventChannel.receiveAsFlow()

    // Used to not handle the same exception more than one time.
    private var lastHandledException: Exception? = null

    fun handleError(exception: Exception, showError: Boolean = true) {
        if (lastHandledException === exception) return
        lastHandledException = exception

        logger.e(exception)
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

        logger.e(exception)
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
