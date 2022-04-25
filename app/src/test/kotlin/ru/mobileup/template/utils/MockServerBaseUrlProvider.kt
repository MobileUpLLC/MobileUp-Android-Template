package ru.mobileup.template.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mobileup.core.network.BaseUrlProvider

@OptIn(ExperimentalCoroutinesApi::class)
class MockServerBaseUrlProvider(private val mockServerRule: MockServerRule) : BaseUrlProvider {

    override fun getUrl(): String = mockServerRule.url ?: ""
}