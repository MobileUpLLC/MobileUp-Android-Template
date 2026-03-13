package ru.mobileup.template.features.pokemons.list

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.mobileup.template.core_testing.network.HttpResponse
import ru.mobileup.template.core_testing.network.RequestMatcher
import ru.mobileup.template.core_testing.network.containsPath
import ru.mobileup.template.core_testing.utils.OutputCapturer
import ru.mobileup.template.features.integrationTest
import ru.mobileup.template.features.pokemons.TestPokemons
import ru.mobileup.template.features.pokemons.createPokemonListComponent
import ru.mobileup.template.features.pokemons.presentation.list.PokemonListComponent

class PokemonListComponentTest : FunSpec({

    context("Pokemon list screen") {

        integrationTest("loads the default type pokemon list") {
            mockServer.enqueue(
                RequestMatcher.containsPath("type/10"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )

            val component = setupComponent { createPokemonListComponent(it, {}) }

            advanceUntilIdle()

            component.pokemonsState.value.loading shouldBe false
            component.pokemonsState.value.data shouldBe TestPokemons.firePokemons
            component.pokemonsState.value.error shouldBe null
        }

        integrationTest("emits pokemon details output when a pokemon is clicked") {
            mockServer.enqueue(
                RequestMatcher.containsPath("type/10"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )

            val capturer = OutputCapturer<PokemonListComponent.Output>()
            val component = setupComponent { createPokemonListComponent(it, capturer) }

            advanceUntilIdle()

            val pokemonId = TestPokemons.detailedPonyta.id
            component.onPokemonClick(pokemonId)

            capturer.last shouldBe PokemonListComponent.Output.PokemonDetailsRequested(pokemonId)
        }

        integrationTest("shows loading during refresh") {
            mockServer.enqueue(
                RequestMatcher.containsPath("type/10"),
                HttpResponse(TestPokemons.firePokemonsJson),
                HttpResponse(
                    TestPokemons.firePokemonsJson,
                    delay = kotlin.time.Duration.parse("1s")
                )
            )

            val component = setupComponent { createPokemonListComponent(it, {}) }
            advanceUntilIdle()

            component.onRefresh()

            runCurrent()

            component.pokemonsState.value.loading shouldBe true

            advanceUntilIdle()

            component.pokemonsState.value.loading shouldBe false
        }
    }
})
