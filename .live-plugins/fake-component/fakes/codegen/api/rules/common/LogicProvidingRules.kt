package fakes.codegen.api.rules.common

import fakes.codegen.api.rules.ProvidingRule

infix fun ProvidingRule.and(
    rule: ProvidingRule
) = ProvidingRule { type ->
    canProvide(type) && rule.canProvide(type)
}

infix fun ProvidingRule.or(
    rule: ProvidingRule
) = ProvidingRule { type ->
    canProvide(type) || rule.canProvide(type)
}

operator fun ProvidingRule.not() = ProvidingRule { type ->
    !canProvide(type)
}