package ru.mobileup.template.core.message

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.message.ui.MessageComponent
import ru.mobileup.template.core.message.ui.RealMessageComponent

fun ComponentFactory.createMessagesComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}