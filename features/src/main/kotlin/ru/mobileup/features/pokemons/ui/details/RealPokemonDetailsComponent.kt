package ru.mobileup.features.pokemons.ui.details

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.single.Replica
import ru.mobileup.core.error_handling.ErrorHandler
import ru.mobileup.core.utils.observe
import ru.mobileup.features.pokemons.domain.DetailedPokemon

class RealPokemonDetailsComponent(
    componentContext: ComponentContext,
    private val pokemonReplica: Replica<DetailedPokemon>,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, PokemonDetailsComponent {

    override val pokemonState by pokemonReplica.observe(lifecycle, errorHandler)

    override fun onRetryClick() {
        pokemonReplica.refresh()
    }

    override fun onRefresh() {
        pokemonReplica.refresh()
    }
}