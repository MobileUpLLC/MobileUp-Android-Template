package ru.mobileup.template.core_testing.network

import io.ktor.http.HttpStatusCode
import kotlin.time.Duration

data class HttpResponse(
    val body: String = "",
    val status: HttpStatusCode = HttpStatusCode.OK,
    val delay: Duration = Duration.ZERO,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json")
)