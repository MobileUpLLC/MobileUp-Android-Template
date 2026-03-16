package ru.mobileup.template.features.pokemons.details

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import ru.mobileup.template.core_testing.network.HttpResponse
import ru.mobileup.template.core_testing.network.RequestMatcher
import ru.mobileup.template.core_testing.network.containsPath
import ru.mobileup.template.features.integrationTest
import ru.mobileup.template.features.pokemons.TestPokemons
import ru.mobileup.template.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.features.pokemons.domain.PokemonType

class PokemonDetailsComponentTest : FunSpec({

    context("Pokemon details screen") {

        integrationTest("loads details successfully") {
            // Prepare a successful details response and create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Verify loaded details are shown
            component.pokemonState.value.data shouldBe TestPokemons.detailedPonyta
            component.pokemonState.value.loading shouldBe false
        }

        integrationTest("shows error when loading fails") {
            // Prepare a failed details response and create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(status = HttpStatusCode.NotFound)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Verify error state is shown
            component.pokemonState.value.error.shouldNotBeNull()
        }

        integrationTest("shows message with types when type is clicked") {
            // Prepare a successful details response and create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }
            advanceUntilIdle()

            // Click a pokemon type
            component.onTypeClick(PokemonType.Fire)

            // Verify message is emitted
            runCurrent()
            testMessageService.last.shouldNotBeNull()
            testMessageService.all.size shouldBe 1
        }

        integrationTest("does not emit message when details are not loaded") {
            // Prepare a failed details response and create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(status = HttpStatusCode.NotFound)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }
            advanceUntilIdle()

            // Click a pokemon type when data is absent
            component.onTypeClick(PokemonType.Fire)

            // Verify no message is emitted
            runCurrent()
            testMessageService.all.shouldBeEmpty()
            testMessageService.isEmpty shouldBe true
        }

        integrationTest("reloads details when retry is clicked") {
            // Prepare initial and retry responses, then create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }
            advanceUntilIdle()

            // Retry loading details
            component.onRetryClick()
            advanceUntilIdle()

            // Verify request is sent again
            mockServer.getRecordedRequests(RequestMatcher.containsPath("pokemon/${pokemonId.value}")).size shouldBe 2
        }

        integrationTest("reloads details when refresh is requested") {
            // Prepare initial and refresh responses, then create the component
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }
            advanceUntilIdle()

            // Refresh loaded details
            component.onRefresh()
            advanceUntilIdle()

            // Verify request is sent again
            mockServer.getRecordedRequests(RequestMatcher.containsPath("pokemon/${pokemonId.value}")).size shouldBe 2
        }
    }
})
