package ru.mobileup.template.features.pokemons.presentation.details

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.template.core.utils.LoadableState
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.domain.PokemonType

interface PokemonDetailsComponent {

    val pokemonState: StateFlow<LoadableState<DetailedPokemon>>

    fun onTypeClick(type: PokemonType)

    fun onRetryClick()

    fun onRefresh()
}
