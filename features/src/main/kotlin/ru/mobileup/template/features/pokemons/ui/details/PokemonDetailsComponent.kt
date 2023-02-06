package ru.mobileup.template.features.pokemons.ui.details

import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.single.Loadable
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon

interface PokemonDetailsComponent {

    val pokemonState: StateFlow<Loadable<DetailedPokemon>>

    fun onRetryClick()

    fun onRefresh()
}
