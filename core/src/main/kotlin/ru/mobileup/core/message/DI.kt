package ru.mobileup.core.message

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.core.ComponentFactory
import ru.mobileup.core.message.ui.MessageComponent
import ru.mobileup.core.message.ui.RealMessageComponent

fun ComponentFactory.createMessagesComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}