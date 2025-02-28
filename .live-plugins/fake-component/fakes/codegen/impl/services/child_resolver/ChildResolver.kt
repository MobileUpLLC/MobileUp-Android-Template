package fakes.codegen.impl.services.child_resolver

import fakes.codegen.api.Processor
import fakes.codegen.api.Service
import fakes.codegen.api.getService
import fakes.codegen.api.typing.ParsedType

data class ChildInfo(
    val name: String,
    val parameterType: ParsedType
)

interface ChildResolver : Service {
    val children: List<ChildInfo>
    val sealedInterfaceName: String
}

val Processor.childResolver get() = getService<ChildResolver>()
