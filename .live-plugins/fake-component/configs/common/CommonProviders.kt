package configs.common

import fakes.codegen.config.PluginConfig
import fakes.codegen.api.provide
import fakes.codegen.api.result
import fakes.codegen.api.rules.common.or
import fakes.codegen.api.rules.common.typeRealNameMatches
import fakes.codegen.api.rules.common.withTopType
import fakes.codegen.api.typing.FakesType
import fakes.codegen.api.typing.ParsedType
import fakes.codegen.impl.services.child_resolver.childResolver
import fakes.codegen.impl.services.package_resolver.packageResolver
import fakes.fqName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.kotlin.name.FqName

val PluginConfig.commonProviders
    get() = listOf(
        withTopType<Unit>() provide "Unit",

        withTopType<Byte>() provide 0.toByte(),
        withTopType<Short>() provide 0.toShort(),
        withTopType<Int>() provide 0,
        withTopType<Long>() provide "0L",
        withTopType<Char>() provide "'a'",
        withTopType<Float>() provide "0f",
        withTopType<Double>() provide "0.0",
        withTopType<Boolean>() provide true,
        withTopType<String>() provide "\"\"",

        withTopType<List<*>>() provide "listOf()",
        withTopType<MutableList<*>>() provide "mutableListOf()",
        withTopType<Set<*>>() provide "setOf()",
        withTopType<MutableSet<*>>() provide "mutableSetOf()",
        withTopType<Map<*, *>>() provide "mapOf()",
        withTopType<MutableMap<*, *>>() provide "mutableMapOf()",

        withTopType<StateFlow<*>>() or withTopType<MutableStateFlow<*>>() provide { type ->
            val flowValueDefault = process(type.appliedGenerics[0])
            val resolvedType = ParsedType(
                type = FakesType.of<MutableStateFlow<*>>(),
                appliedGenerics = type.appliedGenerics
            )

            packageResolver.addImportIfNotRegistered(MutableStateFlow::class.fqName())

            result("${resolvedType.nameInFile}(${flowValueDefault.codeBlock})")
        },

        typeRealNameMatches("^.+Component$") provide { type ->

            val realName = getFakeComponentName(type.type.realName)

            packageResolver.addImportIfNotRegistered(
                type.type.getTypeWithSamePath(
                    filename = realName,
                ).fqName
            )

            result("$realName()")
        },

        withTopType(LoadableState, PagedState) provide { type ->
            val stateDataDefault = process(type.appliedGenerics[0])

            result("${type.nameInFile}(data = ${stateDataDefault.codeBlock})")
        },

        withTopType(InputControl, CheckControl) provide { type ->

            packageResolver.addImportIfNotRegistered(
                FqName(GlobalScope)
            )

            result("${type.nameInFile}(GlobalScope)")
        },

        withTopType(StandardDialogControl) provide { type ->

            packageResolver.addImportIfNotRegistered(
                FqName(FakeStandardDialogControl)
            )

            result("fakeStandardDialogControl()")
        },

        withTopType(SimpleDialogControl) provide { type ->

            val dialogValueDefault = process(type.appliedGenerics[0])

            packageResolver.addImportIfNotRegistered(
                FqName(FakeSimpleDialogControl)
            )

            result("fakeSimpleDialogControl(${dialogValueDefault.codeBlock})")
        },

        withTopType(ChildStack) provide { type ->

            packageResolver.addImportIfNotRegistered(
                FqName(CreateFakeChildStack)
            )

            val sealedInterfaceName = childResolver.sealedInterfaceName
            val child = childResolver.children.first()
            val fakeComponent = process(child.parameterType).codeBlock

            result(
                "createFakeChildStack($componentInterfaceName.$sealedInterfaceName.${child.name}($fakeComponent))"
            )
        },
    )