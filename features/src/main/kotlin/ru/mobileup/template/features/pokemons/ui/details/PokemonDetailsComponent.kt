package ru.mobileup.template.features.pokemons.ui.details

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.template.core.utils.LoadableState
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon

interface PokemonDetailsComponent {

    val pokemonState: StateFlow<LoadableState<DetailedPokemon>>

    fun onRetryClick()

    fun onRefresh()
}
