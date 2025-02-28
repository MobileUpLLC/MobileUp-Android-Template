package fakes.codegen.api.typing

import com.squareup.kotlinpoet.ClassName
import fakes.packageFor
import fakes.packageName
import org.jetbrains.kotlin.name.FqName

data class FakesType(
    val fqName: FqName,
    val alias: String? = null,
    val nullable: Boolean = false
) {
    companion object {
        inline fun <reified T> of(
            alias: String? = null,
            nullable: Boolean = false
        ) = FakesType(
            fqName = FqName(packageFor<T>()),
            alias = alias,
            nullable = nullable
        )
    }

    val nameInFile = alias ?: fqName.pathSegments().last().asString()
    val realName = fqName.pathSegments().last().asString()
    val packageName get() = fqName.packageName

    val importString = buildString {
        append(fqName.asString())
        alias?.let {
            append(" as $it")
        }
    }

    fun getTypeWithSamePath(
        filename: String,
        alias: String? = null
    ) = copy(
        fqName = FqName(
            mutableListOf<String>().apply {
                addAll(fqName.pathSegments().map { it.asString() })
                this[lastIndex] = filename
            }.joinToString(".")
        ),
        alias = alias
    )

    fun asClassName() = ClassName(
        packageName = fqName.packageName,
        fqName.pathSegments().last().asString()
    )
}