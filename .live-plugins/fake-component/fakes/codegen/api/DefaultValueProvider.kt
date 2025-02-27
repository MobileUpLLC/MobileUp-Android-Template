package fakes.codegen.api

import fakes.codegen.api.rules.ProvidingRule
import fakes.codegen.api.typing.ParsedType
import com.squareup.kotlinpoet.CodeBlock

class CannotProvideDefaultException : Exception()

fun interface DefaultValueProvider {
    fun Processor.provide(type: ParsedType): ProcessingResult?
}

infix fun ProvidingRule.provide(
    provider: Processor.(type: ParsedType) -> ProcessingResult
) = DefaultValueProvider { type ->
    if (canProvide(type)) {
        provider(type)
    } else {
        null
    }
}

inline infix fun <reified T> ProvidingRule.provide(
    crossinline provider: Processor.() -> T
) = DefaultValueProvider { type ->
    when {
        !canProvide(type) -> null
        T::class == ProcessingResult::class -> provider() as ProcessingResult
        else -> result(provider().toString())
    }
}

infix fun ProvidingRule.provide(
    providedValue: Any
) = DefaultValueProvider { type ->
    if (canProvide(type)) {
        result(CodeBlock.of(providedValue.toString()))
    } else {
        null
    }
}
