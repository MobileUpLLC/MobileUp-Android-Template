package ru.mobileup.template.features.pokemons

import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.domain.Pokemon
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType
import ru.mobileup.template.features.pokemons.domain.PokemonTypeId
import ru.mobileup.template.features.readTestResource

object TestPokemons {
    val charmanderId = PokemonId("4")
    val ponytaId = PokemonId("77")
    val ponytaTypeId = PokemonTypeId("77")
    val fireTypeId = PokemonType.Fire.id
    val waterTypeId = PokemonType.Water.id

    val firePokemonsJson by lazy { readTestResource("responses/fire_pokemons.json") }

    val firePokemons = listOf(
        Pokemon(id = charmanderId, name = "Charmander"),
        Pokemon(id = ponytaId, name = "Ponyta")
    )

    val detailedPonytaJson by lazy { readTestResource("responses/detailed_ponyta.json") }

    val detailedPonyta = DetailedPokemon(
        id = ponytaId,
        name = "Ponyta",
        height = 1f,
        weight = 30f,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${ponytaId.value}.png",
        types = listOf(PokemonType(ponytaTypeId, "Fire"))
    )
}
