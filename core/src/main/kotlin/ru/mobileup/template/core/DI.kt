package ru.mobileup.template.core

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.template.core.debug_tools.DebugTools
import ru.mobileup.template.core.debug_tools.RealDebugTools
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.data.MessageServiceImpl
import ru.mobileup.template.core.message.ui.MessageComponent
import ru.mobileup.template.core.message.ui.RealMessageComponent
import ru.mobileup.template.core.network.NetworkApiFactory

fun coreModule(backendUrl: String) = module {
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(androidApplication()) }
    single { ReplicaClient(get()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single<DebugTools> { RealDebugTools(androidContext(), get()) }
    single { NetworkApiFactory(backendUrl, get()) }
}

fun ComponentFactory.createMessageComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}