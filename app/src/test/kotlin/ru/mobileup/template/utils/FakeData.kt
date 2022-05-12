package ru.mobileup.template.utils

object FakeData {
    val pokemonListEmptyResponse = """
        {
          "pokemon": []
        }
    """.trimIndent()

    val pokemonListResponse = """
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
            }
          ]
        }
    """.trimIndent()

    val newPokemonListResponse = """
        {
          "pokemon": [
            {
              "pokemon": {
                "name": "squirtle",
                "url": "https://pokeapi.co/api/v2/pokemon/7/"
              },
              "slot": 1
            },
            {
              "pokemon": {
                "name": "wartortle",
                "url": "https://pokeapi.co/api/v2/pokemon/8/"
              },
              "slot": 1
            }
          ]
        }
    """.trimIndent()

    val detailedPokemonResponse = """
        {
          "height": 10,
          "id": 77,
          "name": "ponyta",
          "types": [
            {
              "slot": 1,
              "type": {
                "name": "fire",
                "url": "https://pokeapi.co/api/v2/type/10/"
              }
            }
          ],
          "weight": 300
        }
    """.trimIndent()
}
