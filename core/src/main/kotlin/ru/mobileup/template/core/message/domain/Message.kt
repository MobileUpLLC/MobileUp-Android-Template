package ru.mobileup.template.core.message.domain

import androidx.annotation.DrawableRes
import dev.icerock.moko.resources.desc.StringDesc

data class Message(
    val text: StringDesc,
    @DrawableRes val iconRes: Int? = null,
    val actionTitle: StringDesc? = null,
    val action: (() -> Unit)? = null
)
