package ru.mobileup.core.network

import ru.mobileup.core.error_handling.*
import kotlinx.serialization.SerializationException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ErrorHandlingCall<T>(private val sourceCall: Call<T>) : Call<T> by sourceCall {

    override fun enqueue(callback: Callback<T>) {
        sourceCall.enqueue(getEnqueuedCallback(callback))
    }

    private fun getEnqueuedCallback(callback: Callback<T>) = object : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            when (response.isSuccessful) {
                true -> callback.onResponse(call, response)
                else -> callback.onFailure(call, mapToFailureException(response))
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            callback.onFailure(call, mapToFailureException(throwable))
        }

        private fun mapToFailureException(response: Response<T>) = when (response.code()) {
            HTTP_GATEWAY_TIMEOUT, HTTP_UNAVAILABLE -> NoServerResponseException()
            HTTP_UNAUTHORIZED -> UnauthorizedException(cause = HttpException(response))
            else -> ServerException(cause = HttpException(response))
        }

        private fun mapToFailureException(throwable: Throwable) = when (throwable) {
            is ApplicationException -> throwable
            is SerializationException -> DeserializationException(throwable)
            is SocketTimeoutException -> NoServerResponseException()
            is SSLHandshakeException -> SSLHandshakeException()
            is UnknownHostException, is IOException -> NoInternetException()
            else -> UnknownException(throwable, throwable.message ?: "Unknown")
        }
    }
}