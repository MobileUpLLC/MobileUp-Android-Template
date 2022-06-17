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