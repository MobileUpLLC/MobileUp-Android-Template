package fakes.codegen.impl

import fakes.Config
import fakes.codegen.api.typing.parseType
import fakes.packageName
import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.idea.searching.usages.getDefaultImports
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile

fun generateFake(
    klass: KtClass
): String {
    val file = klass.parent as? KtFile ?: throw RuntimeException("Component must be top-class")
    val name = klass.name ?: throw RuntimeException("Class must have name")

    val fakeName = Config.getFakeComponentName(name)

    val body = klass.body ?: return "class $fakeName : $name"

    val packageResolver = PackageResolverImpl(file.packageFqName)

    val imports = file.importDirectives.map { it.importPath } + file.getDefaultImports()
    imports.filterNotNull().forEach {
        packageResolver.registerImport(
            fqName = it.fqName,
            alias = it.alias?.asString()
        )
    }

    val processor = ProcessorImpl(packageResolver)

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
        val default = processor.process(parsedReturnType)

        resultClass.addFunction(
            FunSpec
                .builder(name)
                .returns(parsedReturnType.asTypeName())
                .addCode("return ${default.codeBlock}")
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
    }

    for (import in packageResolver.addedImports) {
        val className = import.asClassName()
        resultFile.addImport(
            className.packageName,
            className.simpleNames
        )
    }

    return resultFile
        .addType(resultClass.build())
        .build()
        .toString()
}