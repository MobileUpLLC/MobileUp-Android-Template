package ${packageName}.${path}

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import ru.mobileup.template.core.utils.toStateFlow
import com.arkivanov.decompose.router.stack.childStack

class Real${componentName}(
    componentContext: ComponentContext,
    <#if output == true>
    private val onOutput: (${componentName}.Output) -> Unit,
    </#if>
) : ComponentContext by componentContext, ${componentName} {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Default,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)


    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): ${componentName}.Child = when (config) {
        ChildConfig.Default -> ${componentName}.Child.Default
    }

    sealed interface ChildConfig : Parcelable {

        @Parcelize
        data object Default : ChildConfig
    }

}