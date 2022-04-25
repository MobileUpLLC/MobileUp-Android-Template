package ru.mobileup.features.root

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.core.ComponentFactory
import ru.mobileup.features.root.ui.RealRootComponent
import ru.mobileup.features.root.ui.RootComponent

fun ComponentFactory.createRootComponent(componentContext: ComponentContext): RootComponent {
    return RealRootComponent(componentContext, get())
}