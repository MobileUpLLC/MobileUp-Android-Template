package ru.mobileup.features.pokemons.ui.list

import ru.mobileup.features.pokemons.domain.Pokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.domain.PokemonType
import ru.mobileup.features.pokemons.domain.PokemonTypeId
import me.aartikov.replica.single.Loadable

interface PokemonListComponent {

    val types: List<PokemonType>

    val selectedTypeId: PokemonTypeId

    val pokemonsState: Loadable<List<Pokemon>>

    fun onTypeClick(typeId: PokemonTypeId)

    fun onPokemonClick(pokemonId: PokemonId)

    fun onRetryClick()

    sealed interface Output {
        data class PokemonDetailsRequested(val pokemonId: PokemonId) : Output
    }
}