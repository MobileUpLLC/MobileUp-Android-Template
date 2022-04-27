package ru.mobileup.template.utils

import ru.mobileup.core.network.BaseUrlProvider

class MockServerBaseUrlProvider(private val mockServerRule: MockServerRule) : BaseUrlProvider {

    override fun getUrl(): String = mockServerRule.url ?: ""
}