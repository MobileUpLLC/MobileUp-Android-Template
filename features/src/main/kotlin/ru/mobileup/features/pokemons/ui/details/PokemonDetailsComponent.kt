package ru.mobileup.features.pokemons.ui.details

import ru.mobileup.features.pokemons.domain.DetailedPokemon
import me.aartikov.replica.single.Loadable

interface PokemonDetailsComponent {

    val pokemonState: Loadable<DetailedPokemon>

    fun onRetryClick()
}