package ru.mobileup.template.features.pokemons.ui.details

import me.aartikov.replica.single.Loadable
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon

interface PokemonDetailsComponent {

    val pokemonState: Loadable<DetailedPokemon>

    fun onRetryClick()

    fun onRefresh()
}