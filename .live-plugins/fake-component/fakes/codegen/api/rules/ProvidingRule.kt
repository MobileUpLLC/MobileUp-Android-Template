package fakes.codegen.api.rules

import fakes.codegen.api.typing.ParsedType

fun interface ProvidingRule {
    fun canProvide(
        type: ParsedType
    ): Boolean
}