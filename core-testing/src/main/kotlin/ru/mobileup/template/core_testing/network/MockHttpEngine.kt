package ru.mobileup.template.core_testing.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.content.TextContent
import io.ktor.http.headers
import kotlinx.coroutines.CoroutineDispatcher

fun createMockHttpEngine(
    mockServer: MockServer,
    dispatcher: CoroutineDispatcher
): HttpClientEngine {
    return MockEngine(
        MockEngineConfig().apply {
            this.dispatcher = dispatcher
            addHandler { requestData ->
                val request = HttpRequest(
                    method = requestData.method,
                    path = requestData.url.encodedPath,
                    fullUrl = requestData.url.toString(),
                    body = (requestData.body as? TextContent)?.text ?: "",
                    headers = requestData.headers.toMap()
                )
                val mockResponse = mockServer.handleRequest(request)
                respond(
                    content = mockResponse.body,
                    status = mockResponse.status,
                    headers = headers {
                        mockResponse.headers.forEach { (k, v) -> append(k, v) }
                    }
                )
            }
        }
    )
}

private fun Headers.toMap(): Map<String, String> {
    val result = mutableMapOf<String, String>()
    forEach { key, values -> result[key] = values.joinToString() }
    return result
}
