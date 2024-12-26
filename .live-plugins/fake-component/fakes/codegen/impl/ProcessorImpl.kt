package fakes.codegen.impl

import fakes.Config
import fakes.codegen.api.*
import fakes.codegen.api.typing.ParsedType

class ProcessorImpl(
    override val packageResolver: PackageResolver,
) : Processor {

    val providers = Config.providers

    val defaultProvider = DefaultValueProvider { type ->
        MOCKResult(type.nameInFile)
    }

    override fun process(type: ParsedType): ProcessingResult {
        providers.forEach { provider ->
            with(provider) {
                val provided = provide(type)
                if (provided != null) {
                    return provided
                }
            }
        }
        return with(defaultProvider) {
            provide(type)!!
        }
    }
}