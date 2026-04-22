package ru.mobileup.template.shared

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import ru.mobileup.template.core.IosUiProvider
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.utils.RootViewController
import ru.mobileup.template.features.root.presentation.RootUi

fun SharedApp.createRootViewController(): RootViewController {
    val platformUiProvider = IosUiProvider()
    val backDispatcher = BackDispatcher()
    val componentContext = DefaultComponentContext(
        lifecycle = ApplicationLifecycle(),
        stateKeeper = null,
        instanceKeeper = null,
        backHandler = backDispatcher,
    )
    val rootComponent = createRootComponent(componentContext)

    return RootViewController(platformUiProvider, backDispatcher) {
        AppTheme {
            RootUi(rootComponent)
        }
    }
}
