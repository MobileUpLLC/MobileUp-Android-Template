package fakes.codegen.api.typing

import fakes.codegen.impl.PackageResolverImpl

fun PackageResolverImpl.parseType(
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
    val regex = Regex(
        "^(?<type>[^<>]+)(<(?<generic>.+)>)?",
        RegexOption.DOT_MATCHES_ALL
    )
    val matchResult = regex.matchEntire(type)
    return Pair(
        matchResult?.groups?.get("type")?.value ?: type,
        matchResult?.groups?.get("generic")?.value?.split(Regex(",\\s*"))
    )
}
