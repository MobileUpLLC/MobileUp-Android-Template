package ru.mobileup.core

import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.devtools.ReplicaDevTools
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mobileup.core.debug_tools.DebugTools
import ru.mobileup.core.error_handling.ErrorHandler
import ru.mobileup.core.message.data.MessageService
import ru.mobileup.core.message.data.MessageServiceImpl
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.core.network.NetworkApiFactory
import ru.mobileup.core.network.RealBaseUrlProvider

fun coreModule(backendUrl: String) = module {
    single<BaseUrlProvider> { RealBaseUrlProvider(backendUrl) }
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(androidApplication()) }
    single { ReplicaClient(get()) }
    single { ReplicaDevTools(get(), androidContext()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single<DebugTools> { RealDebugTools(androidContext()) }
    single { NetworkApiFactory(get(), get()) }
}