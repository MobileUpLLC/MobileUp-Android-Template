package fakes.codegen.api.rules.common

import fakes.codegen.api.rules.ProvidingRule

fun nameMatches(
    regex: Regex,
) = ProvidingRule { type -> regex.matches(type.nameInFile) }

fun nameMatches(
    regex: String,
): ProvidingRule = nameMatches(Regex(regex))

fun fullPackageMatches(
    regex: Regex,
) = ProvidingRule { type -> regex.matches(type.type.packageName) }

fun fullPackageMatches(
    regex: String,
): ProvidingRule = fullPackageMatches(Regex(regex))

fun typeRealNameMatches(
    regex: Regex,
) = ProvidingRule { type -> regex.matches(type.type.realName) }

fun typeRealNameMatches(
    regex: String,
): ProvidingRule = typeRealNameMatches(Regex(regex))
