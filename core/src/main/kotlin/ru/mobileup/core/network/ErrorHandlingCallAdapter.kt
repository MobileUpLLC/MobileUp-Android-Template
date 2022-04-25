package ru.mobileup.core.network

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class ErrorHandlingCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, Call<R>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<R> = ErrorHandlingCall(sourceCall = call)
}