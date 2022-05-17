package ru.mobileup.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Interceptor

object ToolsInitializer {
    var networkEmulatorInterceptor: Interceptor? = null
        private set

    var chuckerInterceptor: Interceptor? = null
        private set

    var chuckerCollector: ChuckerCollector? = null
        private set

    fun initialize(context: Context) {
        chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = false,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }
}