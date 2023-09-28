package ${packageName}.${path}

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface ${componentName} {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data object Default : Child
    }
<#if output == true>

        sealed interface Output {

        }
</#if>
}