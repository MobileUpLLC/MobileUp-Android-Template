package ru.mobileup.template.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

// import ru.mobileup.template.core.network.createKtorLogger

/**
 * Creates implementations of Ktorfit APIs.
 */
class NetworkApiFactory(
    private val loggingEnabled: Boolean,
    private val backendUrl: String,
    private val httpClientEngine: HttpClientEngine,
    private val errorCollector: ErrorCollector?
) {
    companion object {
        private const val CONNECT_TIMEOUT_MILLISECONDS = 30000L
        private const val READ_WRITE_TIMEOUT_MILLISECONDS = 60000L
    }

    private val json = createDefaultJson()
    private val authorizedHttpClient = createHttpClient(authorized = true)
    private val unauthorizedHttpClient = createHttpClient(authorized = false)

    /**
     * Ktorfit for creating of authorized APIs
     */
    val authorizedKtorfit = createKtorfit(authorizedHttpClient)

    /**
     * Ktorfit for creating of APIs that don't require authorization
     */
    val unauthorizedKtorfit = createKtorfit(unauthorizedHttpClient)

    private fun createHttpClient(authorized: Boolean): HttpClient {
        return HttpClient(httpClientEngine) {
            install(Logging) {
                logger = createKtorLogger()
                level = if (loggingEnabled) LogLevel.ALL else LogLevel.NONE
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT_MILLISECONDS
                requestTimeoutMillis = READ_WRITE_TIMEOUT_MILLISECONDS
            }

            defaultRequest {
                url(backendUrl)
            }

            if (authorized) {
                // TODO: install Auth component and set it up
            }

            setupErrorConverter(errorCollector)
        }
    }

    private fun createKtorfit(httpClient: HttpClient): Ktorfit {
        return Ktorfit.Builder()
            .baseUrl(backendUrl)
            .httpClient(httpClient)
            .build()
    }
}
