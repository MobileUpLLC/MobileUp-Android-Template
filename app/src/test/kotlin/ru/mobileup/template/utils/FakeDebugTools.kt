package ru.mobileup.template.utils

import okhttp3.Interceptor
import ru.mobileup.template.core.debug_tools.DebugTools

class FakeDebugTools : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun launch() {
        // do nothing
    }

    override fun collectNetworkError(exception: Exception) {
        // do nothing
    }
}