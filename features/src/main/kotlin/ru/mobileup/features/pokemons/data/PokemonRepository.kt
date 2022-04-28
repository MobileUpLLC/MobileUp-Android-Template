package ru.mobileup.features.pokemons.data

import me.aartikov.replica.keyed.KeyedReplica
import ru.mobileup.features.pokemons.domain.DetailedPokemon
import ru.mobileup.features.pokemons.domain.Pokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.domain.PokemonTypeId

interface PokemonRepository {

    val pokemonsByTypeReplica: KeyedReplica<PokemonTypeId, List<Pokemon>>

    val pokemonByIdReplica: KeyedReplica<PokemonId, DetailedPokemon>
}