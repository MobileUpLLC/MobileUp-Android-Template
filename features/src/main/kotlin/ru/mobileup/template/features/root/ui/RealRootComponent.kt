package ru.mobileup.template.features.root.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.parcelize.Parcelize
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.createMessageComponent
import ru.mobileup.template.core.utils.toStateFlow
import ru.mobileup.template.features.pokemons.createPokemonsComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Pokemons,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override val messageComponent = componentFactory.createMessageComponent(
        childContext(key = "message")
    )

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is ChildConfig.Pokemons -> {
            RootComponent.Child.Pokemons(
                componentFactory.createPokemonsComponent(componentContext)
            )
        }
    }

    sealed interface ChildConfig : Parcelable {

        @Parcelize
        object Pokemons : ChildConfig
    }
}
