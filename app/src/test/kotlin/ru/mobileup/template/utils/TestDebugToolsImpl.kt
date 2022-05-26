package ru.mobileup.template.utils

import okhttp3.Interceptor
import ru.mobileup.core.debug_tools.DebugTools

class TestDebugToolsImpl : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun collectError(exception: Exception) {
        // do nothing
    }
}