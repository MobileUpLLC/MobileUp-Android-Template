package ru.mobileup.core.network

class RealBaseUrlProvider(private val backendUrl: String) : BaseUrlProvider {

    override fun getUrl(): String = backendUrl
}