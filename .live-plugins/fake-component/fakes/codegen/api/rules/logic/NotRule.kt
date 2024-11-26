package fakes.codegen.api.rules.logic

import fakes.codegen.api.typing.ParsedType
import fakes.codegen.api.rules.ProvidingRule

private class NotRule(
    val source: ProvidingRule,
) : ProvidingRule {
    override fun canProvide(type: ParsedType): Boolean {
        return !source.canProvide(type)
    }
}

operator fun ProvidingRule.not(): ProvidingRule = NotRule(this)
