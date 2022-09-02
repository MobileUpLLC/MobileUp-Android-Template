package ru.mobileup.template.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.core.Koin
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.mobileup.template.allModules
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.debug_tools.DebugTools
import ru.mobileup.template.core.network.NetworkApiFactory

fun withTestKoin(block: (Koin) -> Unit) {
    val koin = Koin()
    koin.loadModules(allModules + createTestModule(koin))
    try {
        block(koin)
    } finally {
        koin.close()
    }
}

private fun createTestModule(koin: Koin, additionDeclarations: ModuleDeclaration? = null) = module {
    single<Context> { ApplicationProvider.getApplicationContext() }
    single { ComponentFactory(koin) }
    single<NetworkConnectivityProvider> { FakeNetworkConnectivityProvider() }
    single<DebugTools> { FakeDebugTools() }
    single { FakeWebServer() } onClose { it?.stopServer() }
    single { NetworkApiFactory(get<FakeWebServer>().url, get()) }
    if (additionDeclarations != null) additionDeclarations()
}

val Koin.componentFactory: ComponentFactory
    get() = this.get()

val Koin.fakeWebServer: FakeWebServer
    get() = this.get()