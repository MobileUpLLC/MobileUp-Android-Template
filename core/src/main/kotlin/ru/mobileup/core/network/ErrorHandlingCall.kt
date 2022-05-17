package ru.mobileup.core.network

import ru.mobileup.core.error_handling.*
import kotlinx.serialization.SerializationException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.mobileup.core.DebugToolsInitializer.chuckerCollector
import java.io.IOException
import java.net.HttpURLConnection.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ErrorHandlingCall<T>(private val sourceCall: Call<T>) : Call<T> by sourceCall {

    override fun enqueue(callback: Callback<T>) {
        sourceCall.enqueue(getEnqueuedCallback(callback))
    }

    @Suppress("DEPRECATION")
    private fun getEnqueuedCallback(callback: Callback<T>) = object : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            when (response.isSuccessful) {
                true -> callback.onResponse(call, response)
                else -> {
                    val exception = mapToFailureException(response)
                    chuckerCollector?.onError("ErrorHandlingCall", exception)
                    callback.onFailure(call, exception)
                }
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            val exception = mapToFailureException(throwable)
            chuckerCollector?.onError("ErrorHandlingCall", exception)
            callback.onFailure(call, exception)
        }

        private fun mapToFailureException(response: Response<T>) = when (response.code()) {
            HTTP_GATEWAY_TIMEOUT, HTTP_UNAVAILABLE -> NoServerResponseException(
                HttpException(response)
            )
            HTTP_UNAUTHORIZED -> UnauthorizedException(HttpException(response))
            else -> ServerException(HttpException(response))
        }

        private fun mapToFailureException(throwable: Throwable) = when (throwable) {
            is ApplicationException -> throwable
            is SerializationException -> DeserializationException(throwable)
            is SocketTimeoutException -> NoServerResponseException(throwable)
            is SSLHandshakeException -> SSLHandshakeException(throwable)
            is UnknownHostException, is IOException -> NoInternetException(throwable)
            else -> UnknownException(throwable, throwable.message ?: "Unknown")
        }
    }
}