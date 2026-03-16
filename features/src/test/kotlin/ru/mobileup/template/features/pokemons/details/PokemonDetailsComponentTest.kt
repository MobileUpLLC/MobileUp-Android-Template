package ru.mobileup.template.features.pokemons.details

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import ru.mobileup.template.core.message.domain.Message
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
            // Prepare a successful details response
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
            // Prepare a failed details response
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
            // Prepare a successful details response
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Start observing message events
            val emittedMessages = mutableListOf<Message>()
            val collectJob = launch {
                testMessageService.messageFlow.collect { emittedMessages += it }
            }
            runCurrent()

            // Click a pokemon type
            component.onTypeClick(PokemonType.Fire)

            // Verify message is emitted
            runCurrent()
            emittedMessages.size shouldBe 1
            emittedMessages.first().shouldNotBeNull()
            collectJob.cancel()
        }

        integrationTest("does not emit message when details are not loaded") {
            // Prepare a failed details response
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(status = HttpStatusCode.NotFound)
            )

            // Create the details component
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Start observing message events
            val emittedMessages = mutableListOf<Message>()
            val collectJob = launch {
                testMessageService.messageFlow.collect { emittedMessages += it }
            }

            // Click a pokemon type when data is absent
            component.onTypeClick(PokemonType.Fire)

            // Verify no message is emitted
            runCurrent()
            emittedMessages.shouldBeEmpty()
            collectJob.cancel()
        }

        integrationTest("reloads details when retry is clicked") {
            // Prepare initial and retry responses
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            // Create the details component
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Retry loading details
            component.onRetryClick()
            advanceUntilIdle()

            // Verify request is sent again
            mockServer.getRecordedRequests(RequestMatcher.containsPath("pokemon/${pokemonId.value}")).size shouldBe 2
        }

        integrationTest("reloads details when refresh is requested") {
            // Prepare initial and refresh responses
            val pokemonId = TestPokemons.ponytaId
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/${pokemonId.value}"),
                HttpResponse(TestPokemons.detailedPonytaJson),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            // Create the details component
            val component = setupComponent { createPokemonDetailsComponent(it, pokemonId) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Refresh loaded details
            component.onRefresh()
            advanceUntilIdle()

            // Verify request is sent again
            mockServer.getRecordedRequests(RequestMatcher.containsPath("pokemon/${pokemonId.value}")).size shouldBe 2
        }
    }
})
