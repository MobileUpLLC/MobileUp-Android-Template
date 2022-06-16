package ru.mobileup.template.core.message.ui

import ru.mobileup.template.core.message.domain.MessageData

interface MessageComponent {

    val visibleMessageData: MessageData?

    fun onActionClick()
}