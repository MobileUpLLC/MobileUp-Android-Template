package ru.mobileup.template.features.pokemons.ui.list

import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.single.Loadable
import ru.mobileup.template.features.pokemons.domain.Pokemon
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType
import ru.mobileup.template.features.pokemons.domain.PokemonTypeId

interface PokemonListComponent {

    val types: List<PokemonType>

    val selectedTypeId: StateFlow<PokemonTypeId>

    val pokemonsState: StateFlow<Loadable<List<Pokemon>>>

    fun onTypeClick(typeId: PokemonTypeId)

    fun onPokemonClick(pokemonId: PokemonId)

    fun onRetryClick()

    fun onRefresh()

    sealed interface Output {
        data class PokemonDetailsRequested(val pokemonId: PokemonId) : Output
    }
}
