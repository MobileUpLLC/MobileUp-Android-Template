package fakes.codegen.impl

import com.squareup.kotlinpoet.*
import fakes.codegen.api.typing.parseType
import fakes.codegen.config.PluginConfig
import fakes.codegen.impl.services.child_resolver.tryCreateChildResolver
import fakes.codegen.impl.services.package_resolver.PackageResolverImpl
import org.jetbrains.kotlin.idea.searching.usages.getDefaultImports
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile

fun generateFake(
    klass: KtClass,
    pluginConfig: PluginConfig
): String {
    val file = klass.parent as? KtFile ?: throw RuntimeException("Component must be top-class")
    val name = klass.name ?: throw RuntimeException("Class must have name")

    val fakeName = pluginConfig.getFakeComponentName(name)

    val body = klass.body ?: return "class $fakeName : $name"

    val packageResolver = PackageResolverImpl(file.packageFqName)

    val imports = file.importDirectives.map { it.importPath } + file.getDefaultImports()
    imports.filterNotNull().forEach {
        packageResolver.registerImport(
            fqName = it.fqName,
            alias = it.alias?.asString()
        )
    }

    val childResolver = tryCreateChildResolver(
        ktClass = klass,
        packageResolver = packageResolver,
        pluginConfig = pluginConfig
    )

    val processor = ProcessorImpl(
        componentInterfaceName = name,
        services = buildList {
            childResolver?.let(::add)
            add(packageResolver)
        },
        pluginConfig = pluginConfig
    )

    val resultFile = FileSpec.builder(
        packageName = file.packageFqName.asString(),
        fileName = fakeName
    )
    val resultClass = TypeSpec
        .classBuilder(
            name = fakeName
        )
        .addSuperinterface(
            ClassName(
                packageName = file.packageFqName.asString(),
                name
            )
        )

    for (property in body.properties) {
        if (property.getter != null) continue

        val type = property.typeReference?.text ?: continue

        val propertyName = property.name ?: continue
        val parsedType = packageResolver.parseType(type)
        val default = processor.process(parsedType)

        resultClass.addProperty(
            PropertySpec
                .builder(
                    name = propertyName,
                    type = parsedType.asTypeName(),
                    modifiers = listOf()
                )
                .initializer(default.codeBlock)
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
    }

    for (function in body.functions) {
        if (function.bodyExpression != null) continue

        val returnType = function.typeReference?.text ?: "Unit"

        val name = function.name ?: continue

        val parsedReturnType = packageResolver.parseType(returnType)

        // Function params
        val params = function.valueParameters.map {
            it.name!! to packageResolver.parseType(it.typeReference?.text!!)
        }

        val default = processor.process(parsedReturnType)

        resultClass.addFunction(
            FunSpec
                .builder(name).apply {
                    returns(parsedReturnType.asTypeName())
                    addCode("return ${default.codeBlock}")
                    addParameters(
                        params.map { (name, parsedType) ->
                            ParameterSpec
                                .builder(name, parsedType.asTypeName())
                                .build()
                        }
                    )
                    addModifiers(KModifier.OVERRIDE)
                }
                .build()
        )
    }

    // Add imports that are required for adding mocks
    for (import in packageResolver.addedImports) {
        val className = import.asClassName()
        resultFile.addImport(
            className.packageName,
            className.simpleNames
        )
    }

    // Nested classes and interfaces
    body.declarations.filterIsInstance<KtClass>().forEach {
        resultFile.addImport(
            file.packageFqName.asString(),
            "${klass.name}.${it.name}"
        )
    }

    return resultFile
        .addType(resultClass.build())
        .build()
        .toString()
}