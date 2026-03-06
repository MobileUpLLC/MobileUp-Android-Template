package ru.mobileup.template.pokemons

import com.arkivanov.essenty.lifecycle.Lifecycle
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.first
import ru.mobileup.template.core.network.MockApiResponse
import ru.mobileup.template.core.utils.getChild
import ru.mobileup.template.features.pokemons.createPokemonsComponent
import ru.mobileup.template.features.pokemons.presentation.PokemonsComponent
import ru.mobileup.template.utils.ComponentFixture
import ru.mobileup.template.utils.ComponentSpec
import ru.mobileup.template.utils.componentFactory
import ru.mobileup.template.utils.createComponentFixture
import ru.mobileup.template.utils.registerFixture
import ru.mobileup.template.utils.testApiDispatcher

class PokemonsComponentTest : ComponentSpec({

    Given("pokemons router component") {

        val fixture: () -> ComponentFixture<PokemonsComponent, Nothing> = registerFixture {
            createComponentFixture { koin, componentContext ->
                koin.componentFactory.createPokemonsComponent(componentContext)
            }
        }

        When("component is created") {
            then("it has list child as initial screen") {
                fixture().sut.childStack.value
                    .getChild<PokemonsComponent.Child.List>()
                    .shouldNotBeNull()
            }
        }

        When("pokemon from list is clicked") {

            beforeTest {
                fixture().koin.testApiDispatcher.setHandler { path ->
                    when (path) {
                        "/api/v2/type/10" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.firePokemonsJson
                        )
                        "/api/v2/pokemon/77" -> MockApiResponse(
                            statusCode = 200,
                            body = TestPokemons.detailedPonytaJson
                        )
                        else -> error("Unexpected request path: $path")
                    }
                }

                fixture().componentContext.moveToState(Lifecycle.State.RESUMED)

                val listChild = fixture().sut.childStack.value
                    .getChild<PokemonsComponent.Child.List>()
                    .shouldNotBeNull()
                listChild.component.onPokemonClick(TestPokemons.detailedPonyta.id)

                fixture().sut.childStack.first {
                    it.getChild<PokemonsComponent.Child.Details>() != null
                }
            }

            then("it navigates to details child") {
                fixture().sut.childStack.value
                    .getChild<PokemonsComponent.Child.Details>()
                    .shouldNotBeNull()
                    .shouldBeInstanceOf<PokemonsComponent.Child.Details>()
            }
        }
    }
})

