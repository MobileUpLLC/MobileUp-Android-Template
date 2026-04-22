package ru.mobileup.template.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

fun createOkHttpEngine(): HttpClientEngine {
    return OkHttp.create()
}