package ru.mobileup.template.core.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import ru.mobileup.template.core.debug_tools.DebugTools
import ru.mobileup.template.core.error_handling.ApplicationException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Converts platform exceptions to [ApplicationException]s. See [ErrorHandlingCall] for more details.
 */
class ErrorHandlingCallAdapterFactory(private val debugTools: DebugTools) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) return null
        check(returnType is ParameterizedType) {}
        return ErrorHandlingCallAdapter<Any>(
            responseType = getParameterUpperBound(0, returnType),
            debugTools = debugTools
        )
    }
}