package ru.mobileup.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import okhttp3.Interceptor

object RealDebugTools {
    var networkEmulatorInterceptor: Interceptor? = null
        private set

    var chuckerInterceptor: Interceptor? = null
        private set

    var chuckerCollector: ChuckerCollector? = null
        private set

    fun initialize(context: Context) {
        // do nothing
    }
}