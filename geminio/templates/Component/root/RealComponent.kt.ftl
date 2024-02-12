package ${packageName}.${packagePath}

import com.arkivanov.decompose.ComponentContext

class Real${componentName}(
    componentContext: ComponentContext,
    <#if output == true>
    private val onOutput: (${componentName}.Output) -> Unit,
    </#if>
) : ComponentContext by componentContext, ${componentName} {

}