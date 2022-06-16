package ru.mobileup.template.features.pokemons.data

import retrofit2.http.GET
import retrofit2.http.Path
import ru.mobileup.template.features.pokemons.data.dto.DetailedPokemonResponse
import ru.mobileup.template.features.pokemons.data.dto.PokemonsByTypeResponse

interface PokemonApi {

    @GET("/api/v2/type/{typeId}")
    suspend fun getPokemonsByType(@Path("typeId") typeId: String): PokemonsByTypeResponse

    @GET("/api/v2/pokemon/{pokemonId}")
    suspend fun getPokemonById(@Path("pokemonId") pokemonId: String): DetailedPokemonResponse
}