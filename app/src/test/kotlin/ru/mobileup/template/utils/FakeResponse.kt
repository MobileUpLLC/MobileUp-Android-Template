package ru.mobileup.template.utils

/**
 * Configuration of a fake response for [FakeWebServer].
 */
sealed interface FakeResponse {

    class Success(val body: String) : FakeResponse

    class Error(val responseCode: Int) : FakeResponse

    companion object {
        val BadRequest = Error(400)
        val Unauthorized = Error(401)
        val NotFound = Error(404)
    }
}

enum class HttpMethod {
    Get, Post, Put, Delete, Patch;

    companion object {
        fun fromString(str: String): HttpMethod? {
            return values().firstOrNull { it.name.equals(str, ignoreCase = true) }
        }
    }
}