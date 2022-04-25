package ru.mobileup.features.pokemons.ui.details

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import ru.mobileup.features.pokemons.domain.DetailedPokemon
import me.aartikov.replica.single.Replica
import ru.mobileup.core.error_handling.ErrorHandler
import ru.mobileup.core.utils.observe

class RealPokemonDetailsComponent(
    componentContext: ComponentContext,
    private val pokemonReplica: Replica<DetailedPokemon>,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, PokemonDetailsComponent {

    override val pokemonState by pokemonReplica.observe(lifecycle, errorHandler)

    override fun onRetryClick() {
        pokemonReplica.refresh()
    }
}