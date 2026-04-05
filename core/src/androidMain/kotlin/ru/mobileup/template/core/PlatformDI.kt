package ru.mobileup.template.core

import android.app.Application
import android.content.Context
import kotlinx.coroutines.Dispatchers
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.dsl.module
import ru.mobileup.template.core.activity.ActivityProvider
import ru.mobileup.template.core.configuration.Configuration
import ru.mobileup.template.core.network.createOkHttpEngine
import ru.mobileup.template.core.settings.AndroidSettingsFactory
import ru.mobileup.template.core.settings.SettingsFactory

actual fun platformCoreModule(configuration: Configuration) = module {
    single { configuration.platform.application }
    single { configuration.platform.debugTools }
    single<Context> { get<Application>() }
    single { ActivityProvider() }
    single { createOkHttpEngine(get()) }
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(get()) }
    single<SettingsFactory> { AndroidSettingsFactory(get(), Dispatchers.IO) }
}