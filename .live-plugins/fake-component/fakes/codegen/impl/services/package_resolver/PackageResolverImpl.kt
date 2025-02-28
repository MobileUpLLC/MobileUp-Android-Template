package fakes.codegen.impl.services.package_resolver

import fakes.codegen.api.typing.FakesType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class PackageResolverImpl(
    private val currentFileFqName: FqName
): PackageResolver {

    private val imports = mutableListOf<FakesType>()
    private val _addedImports = mutableListOf<FakesType>()
    override val addedImports get() = _addedImports.toList()

    override fun addImportIfNotRegistered(
        fqName: FqName
    ): FakesType {
        for (import in imports) {
            if (import.fqName == fqName) {
                return import
            }
        }
        val import = FakesType(fqName)
        _addedImports.add(import)
        return import
    }

    override fun registerImport(
        fqName: FqName,
        alias: String?
    ) {
        imports.add(
            FakesType(
                fqName,
                alias
            )
        )
    }

    override fun resolveByNameInFile(
        name: String
    ): FakesType {
        val clearType = name.replace("?", "")
        val firstName = clearType.split(".").first()
        val nullable = name.contains("?")

        for (import in imports) {
            if (import.nameInFile == firstName) {
                return import.copy(nullable = nullable)
            }
        }

        return FakesType(
            fqName = currentFileFqName.child(Name.identifier(clearType)),
            nullable = nullable
        )
    }
}