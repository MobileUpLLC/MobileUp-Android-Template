package ru.mobileup.template.core_testing.network

import io.ktor.http.HttpMethod

fun interface RequestMatcher {
    fun matches(request: HttpRequest): Boolean

    companion object : RequestMatcher {
        override fun matches(request: HttpRequest): Boolean = true
    }
}

fun RequestMatcher.containsPath(value: String) = RequestMatcher { request ->
    this.matches(request) && request.path.contains(value)
}

fun RequestMatcher.exactPath(value: String) = RequestMatcher { request ->
    this.matches(request) && request.path == value
}

fun RequestMatcher.method(value: HttpMethod) = RequestMatcher { request ->
    this.matches(request) && request.method == value
}
