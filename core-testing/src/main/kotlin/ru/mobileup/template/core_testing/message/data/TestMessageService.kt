package ru.mobileup.template.core_testing.message.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message

class TestMessageService : MessageService {
    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 16)
    var lastMessage: Message? = null
        private set

    override val messageFlow: Flow<Message> = messages

    override fun showMessage(message: Message) {
        lastMessage = message
        messages.tryEmit(message)
    }
}
