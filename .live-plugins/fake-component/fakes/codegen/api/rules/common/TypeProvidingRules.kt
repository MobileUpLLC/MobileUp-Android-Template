package fakes.codegen.api.rules.common

import fakes.codegen.api.rules.ProvidingRule
import fakes.packageFor

inline fun <reified T> withTopType() = ProvidingRule { type ->
    val packageT = packageFor<T>()
    type.type.importString == packageT
}

fun withTopType(vararg packagePath: String) = ProvidingRule { type ->
    packagePath.any { type.type.importString == it }
}
