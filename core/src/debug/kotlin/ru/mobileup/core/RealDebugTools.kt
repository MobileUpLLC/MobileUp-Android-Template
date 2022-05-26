package ru.mobileup.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import me.nemiron.hyperion.networkemulation.NetworkEmulatorInterceptor
import okhttp3.Interceptor
import ru.mobileup.core.debug_tools.DebugTools

class RealDebugTools(context: Context) : DebugTools {

    private val networkEmulatorInterceptor = NetworkEmulatorInterceptor(context)

    private val chuckerCollector = ChuckerCollector(
        context = context,
        showNotification = false,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    private val chuckerInterceptor = ChuckerInterceptor
        .Builder(context)
        .collector(chuckerCollector)
        .build()

    override val interceptors: List<Interceptor> = listOf(
        networkEmulatorInterceptor,
        chuckerInterceptor
    )

    @Suppress("DEPRECATION")
    override fun collectError(exception: Exception) {
        chuckerCollector.onError("RealDebugToolsImpl", exception)
    }
}