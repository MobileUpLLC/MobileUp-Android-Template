package ru.mobileup.template.core

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.template.core.activity.ActivityProvider
import ru.mobileup.template.core.debug_tools.DebugTools
import ru.mobileup.template.core.debug_tools.RealDebugTools
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.data.MessageServiceImpl
import ru.mobileup.template.core.message.ui.MessageComponent
import ru.mobileup.template.core.message.ui.RealMessageComponent
import ru.mobileup.template.core.network.NetworkApiFactory
import ru.mobileup.template.core.network.createOkHttpEngine
import ru.mobileup.template.core.permissions.PermissionService

fun coreModule(backendUrl: String) = module {
    single { ActivityProvider() }
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(get()) }
    single { ReplicaClient(get()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single<DebugTools> { RealDebugTools(get(), get()) }
    single { createOkHttpEngine(get()) }
    single {
        NetworkApiFactory(
            loggingEnabled = BuildConfig.DEBUG,
            backendUrl = backendUrl,
            httpClientEngine = get()
        )
    }
    single(createdAtStart = true) { PermissionService(get(), get()) }
}

fun ComponentFactory.createMessageComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}
