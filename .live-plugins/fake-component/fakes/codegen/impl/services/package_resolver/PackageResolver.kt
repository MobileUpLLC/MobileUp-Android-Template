package fakes.codegen.impl.services.package_resolver

import fakes.codegen.api.Processor
import fakes.codegen.api.Service
import fakes.codegen.api.getService
import fakes.codegen.api.typing.FakesType
import org.jetbrains.kotlin.name.FqName

interface PackageResolver : Service {

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

val Processor.packageResolver get() = getService<PackageResolver>()
