package fakes.codegen.api.rules.common

import fakes.codegen.api.typing.ParsedType
import fakes.codegen.api.rules.ProvidingRule

fun always() = ProvidingRule { _ -> true }
fun never() = ProvidingRule { _ -> false }

fun exactName(name: String) = ProvidingRule { type ->
    name == type.nameInFile
}

fun exactType(type: ParsedType) = ProvidingRule { parsedType ->
    type == parsedType
}
