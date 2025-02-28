package fakes.codegen.api

import fakes.codegen.api.typing.ParsedType
import kotlin.reflect.KClass

interface Processor {
    val componentInterfaceName: String

    fun process(
        type: ParsedType
    ): ProcessingResult

    fun <T : Service> getService(cls: KClass<T>): T

    fun <T : Service> tryGetService(cls: KClass<T>): T?
}

inline fun <reified T : Service> Processor.getService() = getService(T::class)
inline fun <reified T : Service> Processor.tryGetService() = tryGetService(T::class)
