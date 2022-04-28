package ru.mobileup.template

import android.app.Application
import me.aartikov.replica.devtools.ReplicaDevTools
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import ru.mobileup.core.ComponentFactory
import ru.mobileup.core.KoinProvider
import timber.log.Timber

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        initLogger()
        koin = createKoin().also {
            it.declare(ComponentFactory(it))
        }
        launchReplicaDevTools()
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

    private fun launchReplicaDevTools() {
        val devtools = koin.get<ReplicaDevTools>()
        devtools.launch()
    }
}