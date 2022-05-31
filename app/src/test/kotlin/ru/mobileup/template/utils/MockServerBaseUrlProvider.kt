package ru.mobileup.template.utils

class MockServerBaseUrlProvider(private val mockServerRule: MockServerRule) : BaseUrlProvider {

    override fun getUrl(): String = mockServerRule.url ?: ""
}