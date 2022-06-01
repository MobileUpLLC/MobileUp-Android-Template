package ru.mobileup.template.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import ru.mobileup.template.pokemons.FakePokemons

class FakeWebServer {

    private val server = MockWebServer()

    private var isBrokenResponse = false

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return if (isBrokenResponse) {
                MockResponse().setResponseCode(404)
            } else {
                when (request.requestUrl?.encodedPath) {
                    "/api/v2/pokemon/77" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(FakePokemons.detailedPonytaJson)
                    }

                    "/api/v2/type/10" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(FakePokemons.firePokemonsJson)
                    }

                    else -> throw IllegalArgumentException("Unexpected request: $request")
                }
            }
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
        url = ""
    }

    fun changeDispatcherConfiguration(hasBrokenResponse: Boolean) {
        isBrokenResponse = hasBrokenResponse
    }
}