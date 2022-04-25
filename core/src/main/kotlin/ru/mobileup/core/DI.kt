package ru.mobileup.core

import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.mobileup.core.error_handling.ErrorHandler
import ru.mobileup.core.message.data.MessageService
import ru.mobileup.core.message.data.MessageServiceImpl
import ru.mobileup.core.network.NetworkApiFactory

val coreModule = module {
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(androidApplication()) }
    single { ReplicaClient(get()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single { NetworkApiFactory(get()) }
}