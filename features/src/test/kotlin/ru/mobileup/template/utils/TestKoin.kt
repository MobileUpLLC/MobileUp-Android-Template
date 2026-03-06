package ru.mobileup.template.utils

import io.ktor.client.engine.HttpClientEngine
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.message.data.TestMessageService
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.network.NetworkApiFactory
import ru.mobileup.template.core.network.TestApiDispatcher
import ru.mobileup.template.core.network.TestNetworkConnectivityProvider
import ru.mobileup.template.core.network.createMockHttpEngine
import ru.mobileup.template.features.featureModules

class TestKoinContext(
    private val app: KoinApplication
) {
    val koin: Koin
        get() = app.koin

    fun close() {
        app.close()
    }
}

fun createTestKoin(
    baseModules: List<Module> = featureModules,
    moduleDeclaration: ModuleDeclaration? = null
): TestKoinContext {
    val testModule = module {
        single { ComponentFactory(getKoin()) }
        single<NetworkConnectivityProvider> { TestNetworkConnectivityProvider() }
        single { TestApiDispatcher() }
        single<HttpClientEngine> { createMockHttpEngine(get()) }
        single { ReplicaClient(get()) }
        single<MessageService> { TestMessageService() }
        single { ErrorHandler(get()) }
        single {
            NetworkApiFactory(
                loggingEnabled = false,
                backendUrl = "https://test/",
                httpClientEngine = get()
            )
        }
        if (moduleDeclaration != null) moduleDeclaration()
    }

    val app = koinApplication {
        modules(baseModules + testModule)
    }

    return TestKoinContext(app)
}

val Koin.componentFactory: ComponentFactory
    get() = ComponentFactory(this)

val Koin.testApiDispatcher: TestApiDispatcher
    get() = get()

val Koin.testMessageService: TestMessageService
    get() = get<MessageService>() as TestMessageService
