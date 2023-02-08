package ru.mobileup.template

import android.app.Application
import android.content.Context
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.koin.core.Koin
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.KoinProvider
import ru.mobileup.template.core.debug_tools.DebugTools

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        initLogger()
        koin = createKoin()
        launchDebugTools()
    }

    private fun initLogger() {
        if (!BuildConfig.DEBUG) {
            Logger.setMinSeverity(Severity.Assert)
        }
    }

    private fun createKoin(): Koin {
        return Koin().apply {
            loadModules(allModules)
            declare(this@App as Application)
            declare(this@App as Context)
            declare(ComponentFactory(this))
            createEagerInstances()
        }
    }

    private fun launchDebugTools() {
        koin.get<DebugTools>().launch()
    }
}
