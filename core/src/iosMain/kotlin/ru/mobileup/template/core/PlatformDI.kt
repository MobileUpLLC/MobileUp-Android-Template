package ru.mobileup.template.core

import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import ru.mobileup.template.core.settings.IosSettingsFactory
import ru.mobileup.template.core.configuration.Configuration
import ru.mobileup.template.core.settings.SettingsFactory

actual fun platformCoreModule(configuration: Configuration) = module {
    single { Darwin.create() }
    single<SettingsFactory> { IosSettingsFactory(Dispatchers.IO) }
}