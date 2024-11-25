package fakes.codegen.api

import fakes.codegen.api.typing.ParsedType

interface Processor {
    val packageResolver: PackageResolver

    fun process(
        type: ParsedType
    ): ProcessingResult
}
