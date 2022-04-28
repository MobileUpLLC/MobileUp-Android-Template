package ru.mobileup.core.error_handling

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import me.aartikov.sesame.localizedstring.LocalizedString
import ru.mobileup.core.message.data.MessageService
import ru.mobileup.core.message.domain.MessageData
import timber.log.Timber

class ErrorHandler(private val messageService: MessageService) {

    private val unauthorizedEventChannel = Channel<Unit>(Channel.UNLIMITED)

    val unauthorizedEventFlow = unauthorizedEventChannel.receiveAsFlow()

    fun handleError(throwable: Throwable, showError: Boolean = true) {
        Timber.e(throwable)
        when {
            throwable is UnauthorizedException -> {
                unauthorizedEventChannel.trySend(Unit)
            }

            showError -> {
                messageService.showMessage(MessageData(text = throwable.errorMessage))
            }
        }
    }

    fun handleErrorRetryable(
        throwable: Throwable,
        retryActionTitle: LocalizedString,
        retryAction: () -> Unit
    ) {
        Timber.e(throwable)
        when (throwable) {
            is UnauthorizedException -> {
                unauthorizedEventChannel.trySend(Unit)
            }

            else -> {
                messageService.showMessage(
                    MessageData(
                        text = throwable.errorMessage,
                        actionTitle = retryActionTitle,
                        action = retryAction
                    )
                )
            }
        }
    }
}