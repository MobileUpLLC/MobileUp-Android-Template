package ru.mobileup.features.pokemons.ui.details

import me.aartikov.replica.single.Loadable
import ru.mobileup.features.pokemons.domain.DetailedPokemon

interface PokemonDetailsComponent {

    val pokemonState: Loadable<DetailedPokemon>

    fun onRetryClick()

    fun onRefresh()
}