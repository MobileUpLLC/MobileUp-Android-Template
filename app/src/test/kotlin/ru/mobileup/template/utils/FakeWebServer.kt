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
    private val responses = mutableMapOf<String, FakeResponse>()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val fakeResponse = responses[request.requestUrl?.encodedPath] ?: FakeResponse.NotFound
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
    fun prepareResponse(path: String, response: FakeResponse) {
        responses[path] = response
    }
}

/**
 * Handy method to configure a success fake response.
 */
fun FakeWebServer.prepareResponse(path: String, body: String) {
    prepareResponse(path, FakeResponse.Success(body))
}

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