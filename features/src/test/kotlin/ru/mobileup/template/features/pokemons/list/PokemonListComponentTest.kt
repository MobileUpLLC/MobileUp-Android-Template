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
import ru.mobileup.template.features.pokemons.domain.PokemonType
import kotlin.time.Duration.Companion.seconds

class PokemonListComponentTest : FunSpec({

    context("Pokemon list screen") {

        integrationTest("loads the default type pokemon list") {
            // Prepare the default type response and create the list component
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            val component = setupComponent { createPokemonListComponent(it, {}) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Verify the default type list is loaded
            component.pokemonsState.value.loading shouldBe false
            component.pokemonsState.value.data shouldBe TestPokemons.firePokemons
            component.pokemonsState.value.error shouldBe null
        }

        integrationTest("emits pokemon details output when a pokemon is clicked") {
            // Prepare the loaded pokemon list
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            val capturer = OutputCapturer<PokemonListComponent.Output>()
            val component = setupComponent { createPokemonListComponent(it, capturer) }
            advanceUntilIdle()

            // Click a pokemon item in the list
            val pokemonId = TestPokemons.detailedPonyta.id
            component.onPokemonClick(pokemonId)

            // Verify the pokemon details output is emitted
            capturer.last shouldBe PokemonListComponent.Output.PokemonDetailsRequested(pokemonId)
        }

        integrationTest("shows loading during refresh") {
            // Prepare initial data and a delayed refresh response
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson),
                HttpResponse(
                    TestPokemons.firePokemonsJson,
                    delay = 1.seconds
                )
            )
            val component = setupComponent { createPokemonListComponent(it, {}) }
            advanceUntilIdle()

            // Refresh the current list
            component.onRefresh()
            runCurrent()

            // Verify loading is shown during refresh
            component.pokemonsState.value.loading shouldBe true

            // Wait for refresh to complete
            advanceUntilIdle()

            // Verify loading is hidden after refresh
            component.pokemonsState.value.loading shouldBe false
        }

        integrationTest("loads selected type pokemon list when type is clicked") {
            // Prepare responses for default and selected types
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.waterTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            val component = setupComponent { createPokemonListComponent(it, {}) }
            advanceUntilIdle()

            // Select another type and wait for loading
            component.onTypeClick(PokemonType.Water.id)
            advanceUntilIdle()

            // Verify selected type and request are updated
            component.selectedTypeId.value shouldBe PokemonType.Water.id
            mockServer.getRecordedRequests(RequestMatcher.containsPath("type/${TestPokemons.waterTypeId.value}")).size shouldBe 1
        }

        integrationTest("reloads pokemon list when retry is clicked") {
            // Prepare initial and retry responses
            mockServer.enqueue(
                RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}"),
                HttpResponse(TestPokemons.firePokemonsJson),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            val component = setupComponent { createPokemonListComponent(it, {}) }
            advanceUntilIdle()

            // Retry loading the current list
            component.onRetryClick()
            advanceUntilIdle()

            // Verify the request is sent again
            mockServer.getRecordedRequests(RequestMatcher.containsPath("type/${TestPokemons.fireTypeId.value}")).size shouldBe 2
        }
    }
})
