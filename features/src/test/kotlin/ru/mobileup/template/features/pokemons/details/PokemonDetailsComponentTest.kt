package ru.mobileup.template.features.pokemons.details

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import ru.mobileup.template.core_testing.network.HttpResponse
import ru.mobileup.template.core_testing.network.RequestMatcher
import ru.mobileup.template.core_testing.network.containsPath
import ru.mobileup.template.features.integrationTest
import ru.mobileup.template.features.pokemons.TestPokemons
import ru.mobileup.template.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType

class PokemonDetailsComponentTest : FunSpec({

    context("Pokemon details screen") {

        integrationTest("loads details successfully") {
            val pokemonId = PokemonId("77")
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            advanceUntilIdle()

            component.pokemonState.value.data shouldBe TestPokemons.detailedPonyta
            component.pokemonState.value.loading shouldBe false
        }

        integrationTest("shows error when loading fails") {
            val pokemonId = PokemonId("77")
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(status = io.ktor.http.HttpStatusCode.NotFound)
            )

            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            advanceUntilIdle()

            component.pokemonState.value.error.shouldNotBeNull()
        }

        integrationTest("shows message with types when type is clicked") {
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/77"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            val component = setupComponent { createPokemonDetailsComponent(it, PokemonId("77")) }

            advanceUntilIdle()

            component.onTypeClick(PokemonType.Fire)

            testMessageService.lastMessage.shouldNotBeNull()
        }
    }
})
