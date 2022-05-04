package ru.mobileup.template.utils

object FakeData {
    val pokemonsEmptyResponse = """
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
}
