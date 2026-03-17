package ru.mobileup.template.core_testing.network

import kotlinx.coroutines.delay
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration

/**
 * In-memory mock server used by integration tests.
 *
 * Responses are queued as one-time rules and consumed in request order.
 */
class MockServer {
    private val rules = CopyOnWriteArrayList<MockRule>()
    private val recordedRequests = CopyOnWriteArrayList<HttpRequest>()

    /**
     * Enqueues one-time responses for requests matching [matcher].
     *
     * Each response is consumed by exactly one matching request.
     */
    fun enqueue(matcher: RequestMatcher, vararg responses: HttpResponse) {
        responses.forEach { response ->
            rules.add(MockRule(matcher, response))
        }
    }

    /**
     * Returns all recorded requests filtered by [matcher].
     *
     * Prefer asserting user-visible state/output in tests.
     * Use this only when the request itself is the observable behavior
     * (for example, create/update/delete command scenarios).
     */
    fun getRecordedRequests(matcher: RequestMatcher = RequestMatcher): List<HttpRequest> {
        return recordedRequests.filter { matcher.matches(it) }
    }

    internal suspend fun handleRequest(request: HttpRequest): HttpResponse {
        recordedRequests.add(request)

        val rule = rules.firstOrNull { it.matcher.matches(request) }
            ?: error("No mock response enqueued for request: ${request.method.value} ${request.fullUrl}")

        rules.remove(rule)
        val response = rule.response

        if (response.delay > Duration.ZERO) {
            // Works with virtual test time via kotlinx-coroutines-test.
            delay(response.delay)
        }

        return response
    }

    private data class MockRule(
        val matcher: RequestMatcher,
        val response: HttpResponse
    )
}
