package ru.mobileup.template

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import ru.mobileup.core.ComponentFactory
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.core.network.RealBaseUrlProvider
import timber.log.Timber

class App : Application() {

    lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        initLogger()
        koin = createKoin().also {
            it.declare(ComponentFactory(it))
            it.declare(RealBaseUrlProvider(BuildConfig.BACKEND_URL) as BaseUrlProvider)
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createKoin(): Koin {
        return koinApplication {
            androidContext(this@App)
            modules(allModules)
        }.koin
    }
}

val Application.koin get() = (this as App).koin