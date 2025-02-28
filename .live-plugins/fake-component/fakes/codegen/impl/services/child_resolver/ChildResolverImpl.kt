package fakes.codegen.impl.services.child_resolver

import fakes.codegen.api.typing.parseType
import fakes.codegen.config.PluginConfig
import fakes.codegen.impl.services.package_resolver.PackageResolver
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.isAbstract

class ChildResolverImpl(
    override val sealedInterfaceName: String,
    override val children: List<ChildInfo>
) : ChildResolver

fun tryCreateChildResolver(
    ktClass: KtClass,
    pluginConfig: PluginConfig,
    packageResolver: PackageResolver
): ChildResolver? {
    val childInterface = findChildDeclaration(ktClass, pluginConfig)

    val inheritors = childInterface?.let { findInheritors(it, pluginConfig) }

    val childInfos = inheritors?.let {
        mapInheritors(it, packageResolver)
    }?.takeIf(List<*>::isNotEmpty)

    return ChildResolverImpl(
        childInterface?.name ?: return null,
        childInfos ?: return null
    )
}

private fun findChildDeclaration(
    ktClass: KtClass,
    pluginConfig: PluginConfig,
): KtClass? =
    ktClass.body?.children?.find {
        it is KtClass
                && it.isSealed()
                && it.isInterface()
                && pluginConfig.isChild(it.name.toString())
    } as? KtClass

private fun findInheritors(
    ktClass: KtClass,
    pluginConfig: PluginConfig,
) = ktClass.body?.children
    ?.filterIsInstance<KtClass>()
    ?.filter { child ->
        !child.isInterface()
                && !child.isAbstract()
                && child.superTypeListEntries.any { pluginConfig.isChild(it.text) }
    }

private fun mapInheritors(
    inheritors: List<KtClass>,
    packageResolver: PackageResolver
) =
    inheritors.mapNotNull { inheritor ->

        val name = inheritor.name ?: return@mapNotNull null
        val parameter = inheritor.primaryConstructorParameters
            .takeIf { it.size == 1 }
            ?.first()
            ?.typeReference
            ?.text ?: return@mapNotNull null

        ChildInfo(
            name = name,
            parameterType = packageResolver.parseType(parameter)
        )
    }.takeIf(List<*>::isNotEmpty)
