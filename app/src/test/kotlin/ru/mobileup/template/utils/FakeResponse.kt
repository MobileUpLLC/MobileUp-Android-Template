package ru.mobileup.template.utils

sealed interface FakeResponse {

    class Success(val body: String) : FakeResponse

    class Error(val responseCode: Int) : FakeResponse

    companion object {
        val BadRequest = Error(400)
        val NotFound = Error(404)
        val Unauthorized = Error(401)
    }
}