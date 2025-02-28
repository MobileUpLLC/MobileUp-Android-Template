package fakes.codegen.api.typing

import fakes.codegen.impl.services.package_resolver.PackageResolver

fun PackageResolver.parseType(
    typeString: String
): ParsedType {
    val (type, generics) = takeTypeAndGenerics(typeString)

    val resolvedType = resolveByNameInFile(type)
    val parsedGenerics = generics?.map(::parseType)

    return ParsedType(
        type = resolvedType,
        appliedGenerics = parsedGenerics ?: listOf()
    )
}

// Returns type and generic if provided
// List<*> -> Pair("List", "*")
// List<Int> -> Pair("List", "Int")
// List -> Pair("List", null)
fun takeTypeAndGenerics(type: String): Pair<String, List<String>?> {
    var openedBrackets = 0
    val topType = StringBuilder()
    val generics = mutableListOf<StringBuilder>()

    for (char in type) {
        when {
            char == '>' -> {
                if (openedBrackets > 1) {
                    generics.last().append(char)
                }
                openedBrackets--
            }

            char == '<' -> {
                if (openedBrackets == 0) {
                    generics.add(StringBuilder())
                } else {
                    generics.last().append(char)
                }
                openedBrackets++
            }

            openedBrackets == 1 && char == ',' -> generics.add(StringBuilder())
            openedBrackets == 0 -> topType.append(char)
            !char.isWhitespace() -> generics.last().append(char)
        }
    }

    return topType.toString() to generics.takeIf { it.isNotEmpty() }?.map { it.toString() }
}
