package fakes.codegen.api

import fakes.codegen.api.typing.FakesType
import org.jetbrains.kotlin.name.FqName

interface PackageResolver {

    val addedImports: List<FakesType>

    fun addImportIfNotRegistered(
        fqName: FqName
    ): FakesType

    fun registerImport(
        fqName: FqName,
        alias: String? = null
    )

    fun resolveByNameInFile(
        name: String
    ): FakesType
}