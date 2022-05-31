package ru.mobileup.template.pokemons

import ru.mobileup.features.pokemons.domain.*

object FakePokemons {

    val firePokemonsJson = """
        {
          "pokemon": [
            {
              "pokemon": {
                "name": "charmander",
                "url": "https://pokeapi.co/api/v2/pokemon/4/"
              },
              "slot": 1
            },
            {
              "pokemon": {
                "name": "charmeleon",
                "url": "https://pokeapi.co/api/v2/pokemon/5/"
              },
              "slot": 1
            },
            {
              "pokemon": {
                "name": "charizard",
                "url": "https://pokeapi.co/api/v2/pokemon/6/"
              },
              "slot": 1
            },
            {
              "pokemon": {
                "name": "ponyta",
                "url": "https://pokeapi.co/api/v2/pokemon/77/"
              },
              "slot": 1
            }
          ]
        }
    """.trimIndent()

    val firePokemons = listOf(
        Pokemon(
            id = PokemonId("4"),
            name = "Charmander"
        ),
        Pokemon(
            id = PokemonId("5"),
            name = "Charmeleon"
        ),
        Pokemon(
            id = PokemonId("6"),
            name = "Charizard"
        ),
        Pokemon(
            id = PokemonId("77"),
            name = "Ponyta"
        )
    )

    val detailedPonytaJson = """
        {
          "height": 10,
          "id": 77,
          "name": "ponyta",
          "types": [
            {
              "slot": 1,
              "type": {
                "name": "fire",
                "url": "https://pokeapi.co/api/v2/pokemon/77/"
              }
            }
          ],
          "weight": 300
        }
    """.trimIndent()

    val detailedPonyta = DetailedPokemon(
        id = PokemonId("77"),
        name = "Ponyta",
        height = 1f,
        weight = 30f,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/77.png",
        types = listOf(PokemonType(id = PokemonTypeId("77"), name = "Fire"))
    )
}
