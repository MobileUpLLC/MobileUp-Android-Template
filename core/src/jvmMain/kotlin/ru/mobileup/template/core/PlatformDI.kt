package ru.mobileup.template.core

import org.koin.dsl.module
import ru.mobileup.template.core.configuration.BuildType
import ru.mobileup.template.core.configuration.Configuration
import ru.mobileup.template.core.network.createKtorLogger
import ru.mobileup.template.core.network.createOkHttpEngine

actual fun platformCoreModule(configuration: Configuration) = module {
    single { createOkHttpEngine() }
    if (configuration.buildType == BuildType.Debug) {
        single { createKtorLogger() }
    }
}