package fakes

import fakes.codegen.api.provide
import fakes.codegen.api.result
import fakes.codegen.api.rules.common.or
import fakes.codegen.api.rules.common.typeRealNameMatches
import fakes.codegen.api.rules.common.withTopType
import fakes.codegen.api.typing.FakesType
import fakes.codegen.api.typing.ParsedType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.kotlin.name.FqName

object Config {

    fun getFakeComponentName(
        interfaceName: String
    ) = "Fake$interfaceName"

    fun isComponent(
        interfaceName: String
    ) = "Component" in interfaceName

    // TODO: change ...template into your project path
    private const val AppPackage = "ru.mobileup.template"
    private const val LoadableStatePackage = "$AppPackage.core.utils.LoadableState"
    private const val PagedStatePackage = "$AppPackage.core.utils.PagedState"
    private const val StandardDialogControlPackage = "$AppPackage.core.dialog.standard.StandardDialogControl"
    private const val InputControlPackage = "ru.mobileup.kmm_form_validation.control.InputControl"
    private const val CheckControlPackage = "ru.mobileup.kmm_form_validation.control.CheckControl"
    private const val FakeStandardDialogControlPackage = "$AppPackage.core.dialog.standard.fakeStandardDialogControl"

    val providers = listOf(
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

        withTopType(LoadableStatePackage, PagedStatePackage) provide { type ->
            val stateDataDefault = process(type.appliedGenerics[0])

            result("${type.nameInFile}(data = ${stateDataDefault.codeBlock})")
        },

        withTopType(InputControlPackage, CheckControlPackage) provide { type ->

            packageResolver.addImportIfNotRegistered(
                FqName("kotlinx.coroutines.GlobalScope")
            )

            result(
                "${type.nameInFile}(GlobalScope)"
            )
        },

        withTopType(StandardDialogControlPackage) provide { type ->

            packageResolver.addImportIfNotRegistered(
                FqName(FakeStandardDialogControlPackage)
            )

            result("fakeStandardDialogControl()")
        }
    )
}