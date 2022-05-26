package ru.mobileup.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import okhttp3.Interceptor

class RealDebugToolsImpl(context: Context) : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun collectError(exception: Exception) {
        // do nothing
    }
}