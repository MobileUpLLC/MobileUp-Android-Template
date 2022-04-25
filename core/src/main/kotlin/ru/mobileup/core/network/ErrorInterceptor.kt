package ru.mobileup.core.network

import kotlinx.serialization.SerializationException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import ru.mobileup.core.error_handling.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                response
            } else {
                throw HttpException(
                    retrofit2.Response.error<ResponseBody>(
                        response.code,
                        (response.body?.string() ?: "").toResponseBody()
                    )
                )
            }
        } catch (e: Exception) {
            throw when (e) {
                is HttpException -> ServerException(cause = e)

                is SerializationException -> DeserializationException(e)

                is UnknownHostException -> NoInternetException()

                is SocketTimeoutException -> NoServerResponseException()

                is IOException -> NoInternetException()

                else -> UnknownException(message = e.message ?: "", cause = e)
            }
        }
    }
}