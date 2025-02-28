package configs.example

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
import fakes.codegen.api.DefaultValueProvider

val exampleProviders: List<DefaultValueProvider> = listOf(
    // TODO: Define your providers
)