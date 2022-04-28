package ru.mobileup.features.pokemons.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import kotlinx.parcelize.Parcelize
import ru.mobileup.core.ComponentFactory
import ru.mobileup.core.utils.toComposeState
import ru.mobileup.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.ui.list.PokemonListComponent

class RealPokemonsComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, PokemonsComponent {

    private val router = router<ChildConfig, PokemonsComponent.Child>(
        initialConfiguration = ChildConfig.List,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: RouterState<*, PokemonsComponent.Child>
        by router.state.toComposeState(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): PokemonsComponent.Child {
        return when (config) {
            is ChildConfig.List -> {
                PokemonsComponent.Child.List(
                    componentFactory.createPokemonListComponent(
                        componentContext,
                        ::onPokemonListOutput
                    )
                )
            }

            is ChildConfig.Details -> {
                PokemonsComponent.Child.Details(
                    componentFactory.createPokemonDetailsComponent(
                        componentContext,
                        config.pokemonId
                    )
                )
            }
        }
    }

    private fun onPokemonListOutput(output: PokemonListComponent.Output) {
        when (output) {
            is PokemonListComponent.Output.PokemonDetailsRequested -> {
                router.push(ChildConfig.Details(output.pokemonId))
            }
        }
    }

    private sealed interface ChildConfig : Parcelable {

        @Parcelize
        object List : ChildConfig

        @Parcelize
        data class Details(val pokemonId: PokemonId) : ChildConfig
    }
}