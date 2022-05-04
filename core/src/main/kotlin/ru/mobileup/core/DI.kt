package ru.mobileup.core

import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.devtools.ReplicaDevTools
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mobileup.core.error_handling.ErrorHandler
import ru.mobileup.core.message.data.MessageService
import ru.mobileup.core.message.data.MessageServiceImpl
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.core.network.NetworkApiFactory
import ru.mobileup.core.network.RealBaseUrlProvider

fun coreModule(backendUrl: String) = module {
    single<BaseUrlProvider> { RealBaseUrlProvider(backendUrl) }
    single { ReplicaClient() }
    single { ReplicaDevTools(get(), androidContext()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single { NetworkApiFactory(get()) }
}