package ru.mobileup.template.core.network

import retrofit2.Converter
import ru.mobileup.template.core.error_handling.DeserializationException

/**
 * Converts exceptions of JSON-framework to [DeserializationException].
 */
class ErrorHandlingConverter<F : Any, T : Any>(private val converter: Converter<F, T>) :
    Converter<F, T> {

    override fun convert(value: F): T? {
        return try {
            converter.convert(value)
        } catch (e: Exception) {
            throw DeserializationException(e)
        }
    }
}
