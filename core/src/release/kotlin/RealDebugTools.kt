package ru.mobileup.core

import android.content.Context
import okhttp3.Interceptor

class RealDebugTools(context: Context) : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun collectError(exception: Exception) {
        // do nothing
    }
}