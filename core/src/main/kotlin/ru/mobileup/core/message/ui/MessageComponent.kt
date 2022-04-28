package ru.mobileup.core.message.ui

import ru.mobileup.core.message.domain.MessageData

interface MessageComponent {

    val visibleMessageData: MessageData?

    fun onActionClick()
}