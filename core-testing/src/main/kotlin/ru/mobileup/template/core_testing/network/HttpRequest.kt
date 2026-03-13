package ru.mobileup.template.core_testing.network

import io.ktor.http.HttpMethod
import io.ktor.http.Url

data class HttpRequest(
    val method: HttpMethod,
    val path: String,
    val fullUrl: String,
    val body: String,
    val headers: Map<String, String>
) {

    fun queryParam(name: String): String? = Url(fullUrl).parameters[name]
}