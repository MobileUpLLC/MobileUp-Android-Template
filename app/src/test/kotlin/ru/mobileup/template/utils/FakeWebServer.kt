package ru.mobileup.template.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class FakeWebServer {

    private val server = MockWebServer()

    private val responses = mutableMapOf<String, FakeResponse>()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return responses
                .entries
                .firstOrNull { it.key == request.requestUrl?.encodedPath }
                ?.let {
                    when (val fakeResponse = it.value) {
                        is FakeResponse.Success -> {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(body = fakeResponse.body)
                        }

                        is FakeResponse.Error -> {
                            MockResponse().setResponseCode(fakeResponse.responseCode)
                        }
                    }
                } ?: throw IllegalArgumentException("Unexpected request: $request")
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

    fun prepareResponse(path: String, response: FakeResponse) {
        responses[path] = response
    }
}

fun FakeWebServer.prepareResponse(path: String, body: String) {
    prepareResponse(path, FakeResponse.Success(body))
}