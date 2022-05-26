package ru.mobileup.core

import android.content.Context
import okhttp3.Interceptor
import ru.mobileup.core.debug_tools.DebugTools

class RealDebugToolsImpl(context: Context) : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun collectError(exception: Exception) {
        // do nothing
    }
}