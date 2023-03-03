package ru.mobileup.template.core.network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.SerializationException
import ru.mobileup.template.core.error_handling.*

fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setupErrorConverter(
    errorCollector: ErrorCollector?
) {
    expectSuccess = true

    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            val exception = convertToApplicationException(cause)
            errorCollector?.collectNetworkError(exception)
            throw exception
        }
    }
}

private suspend fun convertToApplicationException(throwable: Throwable) = when (throwable) {
    is ApplicationException -> throwable
    is ClientRequestException -> when (throwable.response.status) {
        HttpStatusCode.GatewayTimeout, HttpStatusCode.ServiceUnavailable -> {
            NoServerResponseException(throwable)
        }
        HttpStatusCode.Unauthorized -> UnauthorizedException(throwable)
        else -> mapBadRequestException(throwable)
    }
    is ContentConvertException -> DeserializationException(throwable)
    is SocketTimeoutException -> NoServerResponseException(throwable)
    is IOException -> (throwable.cause as? ApplicationException) ?: NoInternetException(throwable)
    else -> UnknownException(throwable, throwable.message ?: "Unknown")
}

private suspend fun mapBadRequestException(exception: ClientRequestException): ApplicationException =
    try {
        val json = createDefaultJson()
        val errorBody = exception.response.bodyAsText()
        val errorMessage = null // TODO: parse errorBody

        ServerException(exception, errorMessage)
    } catch (e: Exception) {
        when (e) {
            is SerializationException, is IllegalArgumentException -> DeserializationException(e)
            else -> ServerException(e)
        }
    }