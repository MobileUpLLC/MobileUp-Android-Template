package fakes.codegen.config

import fakes.codegen.api.DefaultValueProvider

interface PluginConfig {
    val appPackage: String
    val providers: List<DefaultValueProvider>

    fun getFakeComponentName(interfaceName: String): String

    fun isChild(interfaceName: String): Boolean
    fun isComponent(interfaceName: String): Boolean
}