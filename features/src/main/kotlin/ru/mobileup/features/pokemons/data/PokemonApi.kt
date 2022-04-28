package ru.mobileup.features.pokemons.data

import ru.mobileup.features.pokemons.data.dto.DetailedPokemonResponse
import ru.mobileup.features.pokemons.data.dto.PokemonsByTypeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("/api/v2/type/{typeId}")
    suspend fun getPokemonsByType(@Path("typeId") typeId: String): PokemonsByTypeResponse

    @GET("/api/v2/pokemon/{pokemonId}")
    suspend fun getPokemonById(@Path("pokemonId") pokemonId: String): DetailedPokemonResponse
}