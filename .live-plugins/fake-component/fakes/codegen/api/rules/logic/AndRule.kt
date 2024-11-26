package fakes.codegen.api.rules.logic

import fakes.codegen.api.typing.ParsedType
import fakes.codegen.api.rules.ProvidingRule

private class AndRule(
    val first: ProvidingRule,
    val second: ProvidingRule,
) : ProvidingRule {
    override fun canProvide(type: ParsedType): Boolean {
        return first.canProvide(type) && second.canProvide(type)
    }
}


