package ru.mobileup.template.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer

class FakeWebServer {

    private val server = MockWebServer()

    var url: String = ""
        private set

    init {
        server.start()
        url = server.url("/").toString()
    }

    fun stopServer() {
        server.shutdown()
        url = ""
    }

    fun setDispatcher(dispatcher: Dispatcher) {
        server.dispatcher = dispatcher
    }
}