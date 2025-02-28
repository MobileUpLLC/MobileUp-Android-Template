package fakes.codegen.impl

import fakes.codegen.api.*
import fakes.codegen.api.typing.ParsedType
import fakes.codegen.config.PluginConfig
import kotlin.reflect.KClass

class ProcessorImpl(
    override val componentInterfaceName: String,
    private val services: List<Service>,
    private val pluginConfig: PluginConfig
) : Processor {

    private val defaultProvider = DefaultValueProvider { type ->
        MOCKResult(type.nameInFile)
    }

    override fun process(type: ParsedType): ProcessingResult {
        pluginConfig.providers.forEach { provider ->
            val provided = useProvider(
                type,
                provider
            )
            if (provided != null) {
                return provided
            }
        }
        return useProvider(
            type,
            defaultProvider
        ) ?: throw RuntimeException("Default provider mustn`t return null")
    }

    private fun useProvider(type: ParsedType, provider: DefaultValueProvider): ProcessingResult? {
        val result = runCatching {
            with(provider) { provide(type) }
        }

        return result.getOrNull()
            ?: when (val exception = result.exceptionOrNull()) {
                null,
                is ServiceNotFound,
                is CannotProvideDefaultException -> null

                else -> throw exception
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Service> tryGetService(cls: KClass<T>): T? {
        return services.find { cls.isInstance(it) } as? T
    }

    override fun <T : Service> getService(cls: KClass<T>): T {
        return tryGetService(cls) ?: throw ServiceNotFound()
    }
}