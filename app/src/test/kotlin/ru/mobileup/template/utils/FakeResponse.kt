package ru.mobileup.template.utils

sealed interface FakeResponse {

    class Success(val body: String) : FakeResponse

    class Error(val responseCode: Int) : FakeResponse

    companion object {
        val BadRequest = Error(400)
    }
}