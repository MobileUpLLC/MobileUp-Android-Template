package ru.mobileup.template.core.network

import retrofit2.Call
import retrofit2.CallAdapter
import ru.mobileup.template.core.debug_tools.DebugTools
import java.lang.reflect.Type

internal class ErrorHandlingCallAdapter<R>(
    private val responseType: Type,
    private val debugTools: DebugTools
) : CallAdapter<R, Call<R>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<R> = ErrorHandlingCall(
        sourceCall = call,
        debugTools = debugTools
    )
}