package fakes.codegen.api.typing

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName

class ParsedType(
    val type: FakesType,
    val appliedGenerics: List<ParsedType> = listOf()
) {
    val isParameterized get() = appliedGenerics.isNotEmpty()
    val nameInFile get() = type.nameInFile

    fun asTypeName(): TypeName {

        if (type.realName == "*") return STAR

        val className = type.asClassName()

        if (appliedGenerics.isEmpty()) return className.copy(nullable = type.nullable)

        return className.parameterizedBy(
            *appliedGenerics.map { it.asTypeName() }.toTypedArray()
        ).copy(nullable = type.nullable)
    }

    override fun toString(): String {
        return buildString {
            append(type.nameInFile)
            if (isParameterized) {
                append("<")
                append(appliedGenerics.joinToString(separator = ", "))
                append(">")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParsedType

        if (type != other.type) return false
        if (appliedGenerics != other.appliedGenerics) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + appliedGenerics.hashCode()
        return result
    }
}
