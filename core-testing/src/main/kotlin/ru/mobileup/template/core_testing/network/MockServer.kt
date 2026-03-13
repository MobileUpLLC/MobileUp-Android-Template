package ru.mobileup.template.core_testing.network

import kotlinx.coroutines.delay
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration

class MockServer {
    private val rules = CopyOnWriteArrayList<MockRule>()
    private val recordedRequests = CopyOnWriteArrayList<HttpRequest>()

    fun enqueue(matcher: RequestMatcher, vararg responses: HttpResponse) {
        rules.add(MockRule(matcher, responses.toMutableList()))
    }

    fun getRecordedRequests(matcher: RequestMatcher = RequestMatcher): List<HttpRequest> {
        return recordedRequests.filter { matcher.matches(it) }
    }

    internal suspend fun handleRequest(request: HttpRequest): HttpResponse {
        recordedRequests.add(request)

        val rule = rules.find { it.matcher.matches(request) }
            ?: error("No mock response enqueued for request: ${request.method.value} ${request.fullUrl}")

        val response = if (rule.responses.size > 1) {
            rule.responses.removeAt(0)
        } else {
            rule.responses.first()
        }

        if (response.delay > Duration.ZERO) {
            // Работает с виртуальным временем в тестах
            delay(response.delay)
        }

        return response
    }

    private data class MockRule(
        val matcher: RequestMatcher,
        val responses: MutableList<HttpResponse>
    )
}