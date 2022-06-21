package ru.mobileup.template.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.mobileup.template.core.BuildConfig
import ru.mobileup.template.core.debug_tools.DebugTools
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Creates implementations of Retrofit APIs.
 */
class NetworkApiFactory(
    private val url: String,
    private val debugTools: DebugTools
) {

    companion object {
        private const val CONNECT_TIMEOUT_SECONDS = 30L
        private const val READ_TIMEOUT_SECONDS = 60L
        private const val WRITE_TIMEOUT_SECONDS = 60L
    }

    private val json = createJson()
    private val authorizedOkHttpClient = createOkHttpClient(authorized = true)
    private val unauthorizedOkHttpClient = createOkHttpClient(authorized = false)

    private val authorizedRetrofit = createRetrofit(authorizedOkHttpClient)
    private val unauthorizedRetrofit = createRetrofit(unauthorizedOkHttpClient)

    /**
     * Creates an api that requires authorization
     */
    inline fun <reified T : Any> createAuthorizedApi(): T = createAuthorizedApi(T::class.java)

    /**
     * Creates an API that doesn't require authorization
     */
    inline fun <reified T : Any> createUnauthorizedApi(): T = createUnauthorizedApi(T::class.java)

    fun <T : Any> createAuthorizedApi(clazz: Class<T>): T {
        return authorizedRetrofit.create(clazz)
    }

    fun <T : Any> createUnauthorizedApi(clazz: Class<T>): T {
        return unauthorizedRetrofit.create(clazz)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory(debugTools))
            .addConverterFactory(ErrorHandlingConverterFactory(json.asConverterFactory("application/json".toMediaType())))
            .build()
    }

    private fun createOkHttpClient(authorized: Boolean): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)

                if (authorized) {
                    addInterceptor(AuthorizationInterceptor())
                }

                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(createLoggingInterceptor())
                    debugTools.interceptors.forEach { addInterceptor(it) }
                }
            }
            .build()
    }

    private fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createJson(): Json {
        return Json {
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}