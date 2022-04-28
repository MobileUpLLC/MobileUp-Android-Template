package ru.mobileup.core.message.data

import ru.mobileup.core.message.domain.MessageData
import kotlinx.coroutines.flow.Flow

interface MessageService {

    val messageFlow: Flow<MessageData>

    fun showMessage(message: MessageData)
}