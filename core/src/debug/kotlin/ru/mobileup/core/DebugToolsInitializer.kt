package ru.mobileup.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import me.nemiron.hyperion.networkemulation.NetworkEmulatorInterceptor
import okhttp3.Interceptor

object DebugToolsInitializer {
    var networkEmulatorInterceptor: Interceptor? = null
        private set

    var chuckerInterceptor: Interceptor? = null
        private set

    var chuckerCollector: ChuckerCollector? = null
        private set

    fun initialize(context: Context) {
        networkEmulatorInterceptor = NetworkEmulatorInterceptor(context)

        chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = false,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        chuckerInterceptor = ChuckerInterceptor
            .Builder(context)
            .collector(chuckerCollector!!)
            .build()
    }
}