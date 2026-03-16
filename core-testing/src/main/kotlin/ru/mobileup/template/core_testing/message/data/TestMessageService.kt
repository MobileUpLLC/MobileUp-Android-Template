package ru.mobileup.template.core_testing.message.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message

/**
 * Test implementation of [MessageService].
 *
 * Exposes a hot message flow with a small buffer so tests can assert emitted messages
 * without depending on additional mutable state.
 */
class TestMessageService : MessageService {
    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 16)

    override val messageFlow: Flow<Message> = messages

    /**
     * Emits a message in a non-suspending way for deterministic tests.
     */
    override fun showMessage(message: Message) {
        messages.tryEmit(message)
    }
}
