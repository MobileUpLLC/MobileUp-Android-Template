package ru.mobileup.template.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

/**
 * Starts a local web server with configurable fake responses.
 */
class FakeWebServer {

    private val server = MockWebServer()
    private val responses = mutableMapOf<RequestKey, FakeResponse>()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val fakeResponse = responses[request.requestKey] ?: FakeResponse.NotFound
            return fakeResponse.toMockResponse()
        }
    }

    var url: String = ""
        private set

    init {
        server.start()
        server.dispatcher = dispatcher
        url = server.url("/").toString()
    }

    fun stopServer() {
        server.shutdown()
        responses.clear()
        url = ""
    }

    /**
     * Configures a fake response for a given request path.
     */
    fun prepare(method: HttpMethod, path: String, response: FakeResponse) {
        responses[RequestKey(method, path)] = response
    }
}

/**
 * Handy method to configure a success fake response.
 */
fun FakeWebServer.prepare(method: HttpMethod, path: String, body: String) {
    prepare(method, path, FakeResponse.Success(body))
}

private data class RequestKey(
    val method: HttpMethod,
    val path: String
)

private fun FakeResponse.toMockResponse() = when (this) {
    is FakeResponse.Success -> {
        MockResponse()
            .setResponseCode(200)
            .setBody(body)
    }

    is FakeResponse.Error -> {
        MockResponse().setResponseCode(responseCode)
    }
}

private val RecordedRequest.requestKey: RequestKey?
    get() {
        val methodStr = method ?: return null
        val method = HttpMethod.fromString(methodStr) ?: return null
        val path = requestUrl?.encodedPath ?: return null
        return RequestKey(method, path)
    }

