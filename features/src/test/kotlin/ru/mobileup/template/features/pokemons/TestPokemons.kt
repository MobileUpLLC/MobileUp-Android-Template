package ru.mobileup.template.features.pokemons

import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.domain.Pokemon
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType
import ru.mobileup.template.features.pokemons.domain.PokemonTypeId
import ru.mobileup.template.features.readTestResource

object TestPokemons {
    val ponytaId = PokemonId("77")

    val firePokemonsJson by lazy { readTestResource("responses/fire_pokemons.json") }

    val firePokemons = listOf(
        Pokemon(id = PokemonId("4"), name = "Charmander"),
        Pokemon(id = PokemonId("77"), name = "Ponyta")
    )

    val detailedPonytaJson by lazy { readTestResource("responses/detailed_ponyta.json") }

    val detailedPonyta = DetailedPokemon(
        id = ponytaId,
        name = "Ponyta",
        height = 1f,
        weight = 30f,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/77.png",
        types = listOf(PokemonType(PokemonTypeId("77"), "Fire"))
    )
}
