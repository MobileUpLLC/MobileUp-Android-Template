package ru.mobileup.template.core.message.domain

import org.jetbrains.compose.resources.DrawableResource
import ru.mobileup.template.core.utils.StringDesc

data class Message(
    val text: StringDesc,
    val type: MessageType,
    val iconRes: DrawableResource? = null,
    val actionTitle: StringDesc? = null,
    val action: (() -> Unit)? = null
)

enum class MessageType {
    Positive, Negative, Neutral
}