package ru.mobileup.template.core.message.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.mobileup.template.core.message.domain.Message

class TestMessageService : MessageService {
    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 16)

    override val messageFlow: Flow<Message> = messages

    override fun showMessage(message: Message) {
        messages.tryEmit(message)
    }
}
