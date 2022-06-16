package ru.mobileup.template.core.message.data

import kotlinx.coroutines.flow.Flow
import ru.mobileup.template.core.message.domain.MessageData

interface MessageService {

    val messageFlow: Flow<MessageData>

    fun showMessage(message: MessageData)
}