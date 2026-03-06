package ru.mobileup.template.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode

data class MockApiResponse(
    val statusCode: Int,
    val body: String = "",
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json")
)

class TestApiDispatcher {

    @Volatile
    private var handler: (path: String) -> MockApiResponse = { path ->
        error("Unexpected request path: $path")
    }

    fun setHandler(handler: (path: String) -> MockApiResponse) {
        this.handler = handler
    }

    internal fun dispatch(path: String): MockApiResponse = handler(path)
}

fun createMockHttpEngine(dispatcher: TestApiDispatcher): HttpClientEngine {
    return MockEngine { request ->
        val response = dispatcher.dispatch(request.url.encodedPath)

        respond(
            content = response.body,
            status = HttpStatusCode.fromValue(response.statusCode),
            headers = Headers.build {
                response.headers.forEach { (name, value) ->
                    append(name, value)
                }
            }
        )
    }
}
