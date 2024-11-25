package fakes.codegen.api.rules.logic

import fakes.codegen.api.typing.ParsedType
import fakes.codegen.api.rules.ProvidingRule

private class OrRule(
    val first: ProvidingRule,
    val second: ProvidingRule,
) : ProvidingRule {
    override fun canProvide(type: ParsedType): Boolean {
        return first.canProvide(type) || second.canProvide(type)
    }
}

infix fun ProvidingRule.or(rule: ProvidingRule): ProvidingRule = OrRule(this, rule)
