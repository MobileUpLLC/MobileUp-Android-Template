package ru.mobileup.template.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MockServerRule(private val dispatcher: Dispatcher? = null) : TestWatcher() {

    val server = MockWebServer()

    var url: String? = null
        private set

    override fun starting(description: Description) {
        super.starting(description)
        dispatcher?.let { server.dispatcher = it }
        server.start()
        url = server.url("/").toString()
    }

    override fun finished(description: Description) {
        server.shutdown()
        url = null
        super.finished(description)
    }
}