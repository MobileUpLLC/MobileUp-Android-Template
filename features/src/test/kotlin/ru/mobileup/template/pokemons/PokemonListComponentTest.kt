package ru.mobileup.template.pokemons

import app.cash.turbine.test
import com.arkivanov.essenty.lifecycle.Lifecycle
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import ru.mobileup.template.core.network.MockApiResponse
import ru.mobileup.template.features.pokemons.createPokemonListComponent
import ru.mobileup.template.features.pokemons.presentation.list.PokemonListComponent
import ru.mobileup.template.utils.ComponentFixture
import ru.mobileup.template.utils.ComponentSpec
import ru.mobileup.template.utils.awaitMatching
import ru.mobileup.template.utils.componentFactory
import ru.mobileup.template.utils.createComponentFixtureWithOutput
import ru.mobileup.template.utils.registerFixture
import ru.mobileup.template.utils.testApiDispatcher
import kotlin.time.Duration.Companion.seconds

class PokemonListComponentTest : ComponentSpec({

    Given("pokemon list screen") {

        val fixture: () -> ComponentFixture<PokemonListComponent, PokemonListComponent.Output> = registerFixture {
            createComponentFixtureWithOutput { koin, componentContext, onOutput ->
                koin.componentFactory.createPokemonListComponent(componentContext, onOutput)
            }
        }

        When("initial fire pokemons are loaded successfully") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/type/10" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.firePokemonsJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonsState.first { !it.loading }
            }

            then("it shows loaded pokemons for the first tab") {
                fixture().sut.pokemonsState.value.loading shouldBe false
                fixture().sut.pokemonsState.value.data shouldBe TestPokemons.firePokemons
                fixture().sut.pokemonsState.value.error shouldBe null
            }
        }

        When("a pokemon is clicked") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/type/10" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.firePokemonsJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.onPokemonClick(TestPokemons.detailedPonyta.id)
            }

            then("it emits navigation output to details") {
                fixture().output shouldBe PokemonListComponent.Output.PokemonDetailsRequested(
                    TestPokemons.detailedPonyta.id
                )
            }
        }

        When("loading fails") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/type/10" -> MockApiResponse(statusCode = 404)
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonsState.first { !it.loading }
            }

            then("it exposes fullscreen error state") {
                fixture().sut.pokemonsState.value.error.shouldNotBeNull()
            }
        }

        When("retry is clicked after failed loading") {

            beforeTest {
                var isFirstResponse = true
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when {
                        path != "/api/v2/type/10" -> error("Unexpected request path: $path")
                        isFirstResponse -> {
                            isFirstResponse = false
                            MockApiResponse(statusCode = 404)
                        }
                        else -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.firePokemonsJson
                        )
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)
                fixture().sut.pokemonsState.first { !it.loading }
            }

            then("it reloads pokemons successfully") {
                fixture().sut.pokemonsState.test(timeout = 5.seconds) {
                    awaitMatching { it.error != null }

                    fixture().sut.onRetryClick()

                    awaitMatching { it.loading }
                    val dataState = awaitMatching { !it.loading && it.data != null }
                    dataState.data shouldBe TestPokemons.firePokemons
                    dataState.error shouldBe null
                }
            }
        }
    }
})
