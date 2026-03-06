package ru.mobileup.template.pokemons

import app.cash.turbine.test
import com.arkivanov.essenty.lifecycle.Lifecycle
import io.kotest.assertions.timing.eventually
import kotlinx.coroutines.flow.first
import ru.mobileup.template.core.network.MockApiResponse
import ru.mobileup.template.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.features.pokemons.domain.PokemonType
import ru.mobileup.template.features.pokemons.presentation.details.PokemonDetailsComponent
import ru.mobileup.template.utils.ComponentFixture
import ru.mobileup.template.utils.ComponentSpec
import ru.mobileup.template.utils.awaitMatching
import ru.mobileup.template.utils.componentFactory
import ru.mobileup.template.utils.createComponentFixture
import ru.mobileup.template.utils.registerFixture
import ru.mobileup.template.utils.testApiDispatcher
import ru.mobileup.template.utils.testMessageService
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.seconds

class PokemonDetailsComponentTest : ComponentSpec({

    Given("pokemon details screen") {

        val fixture: () -> ComponentFixture<PokemonDetailsComponent, Nothing> = registerFixture {
            createComponentFixture { koin, componentContext ->
                koin.componentFactory.createPokemonDetailsComponent(
                    componentContext,
                    TestPokemons.detailedPonyta.id
                )
            }
        }

        When("details are loaded successfully for provided id") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/pokemon/77" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.detailedPonytaJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading }
            }

            then("it shows loaded detailed pokemon") {
                fixture().sut.pokemonState.value.loading shouldBe false
                fixture().sut.pokemonState.value.data shouldBe TestPokemons.detailedPonyta
                fixture().sut.pokemonState.value.error shouldBe null
            }
        }

        When("loading fails") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/pokemon/77" -> MockApiResponse(statusCode = 404)
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading }
            }

            then("it exposes fullscreen error state") {
                fixture().sut.pokemonState.value.error.shouldNotBeNull()
            }
        }

        When("retry is clicked after failed loading") {

            beforeTest {
                var isFirstResponse = true
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when {
                        path != "/api/v2/pokemon/77" -> error("Unexpected request path: $path")
                        isFirstResponse -> {
                            isFirstResponse = false
                            MockApiResponse(statusCode = 404)
                        }
                        else -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.detailedPonytaJson
                        )
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading }
            }

            then("it reloads details successfully") {
                fixture().sut.pokemonState.test(timeout = 5.seconds) {
                    awaitMatching { it.error != null }

                    fixture().sut.onRetryClick()

                    awaitMatching { it.loading }
                    val dataState = awaitMatching { !it.loading && it.data != null }
                    dataState.data shouldBe TestPokemons.detailedPonyta
                    dataState.error shouldBe null
                }
            }
        }

        When("type is clicked after pokemon is loaded") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/pokemon/77" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.detailedPonytaJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading && it.data != null }
            }

            then("it sends message with pokemon types") {
                fixture().koin.testMessageService.messageFlow.test(timeout = 5.seconds) {
                    fixture().sut.onTypeClick(PokemonType.Fire)
                    awaitItem().shouldNotBeNull()
                }
            }
        }

        When("type is clicked when pokemon data is not loaded") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/pokemon/77" -> MockApiResponse(statusCode = 404)
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading && it.error != null }
            }

            then("it does not emit any message") {
                fixture().koin.testMessageService.messageFlow.test(timeout = 5.seconds) {
                    fixture().sut.onTypeClick(PokemonType.Fire)
                    expectNoEvents()
                }
            }
        }

        When("refresh is clicked") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/pokemon/77" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.detailedPonytaJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonState.first { !it.loading && it.data != null }
            }

            then("it reloads pokemon details") {
                fixture().sut.onRefresh()

                eventually(5.seconds) {
                    fixture().sut.pokemonState.value.data shouldBe TestPokemons.detailedPonyta
                    fixture().sut.pokemonState.value.error shouldBe null
                }
            }
        }
    }
})
