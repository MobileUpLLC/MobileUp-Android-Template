package ru.mobileup.template.features.pokemons

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import ru.mobileup.template.core_testing.network.HttpResponse
import ru.mobileup.template.core_testing.network.RequestMatcher
import ru.mobileup.template.core_testing.network.containsPath
import ru.mobileup.template.features.integrationTest
import ru.mobileup.template.features.pokemons.presentation.PokemonsComponent

class PokemonsComponentTest : FunSpec({

    context("Pokemons сomponent") {

        integrationTest("shows list as initial screen") {
            // Create the pokemons router component
            val component = setupComponent { createPokemonsComponent(it) }

            // Verify list screen is shown initially
            component.childStack.value.active.instance.shouldBeInstanceOf<PokemonsComponent.Child.List>()
        }

        integrationTest("navigates to details when pokemon is clicked in the list") {
            // Prepare list and details responses
            mockServer.enqueue(
                RequestMatcher.containsPath("type/10"),
                HttpResponse(TestPokemons.firePokemonsJson)
            )
            mockServer.enqueue(
                RequestMatcher.containsPath("pokemon/4"),
                HttpResponse(TestPokemons.detailedPonytaJson)
            )

            // Create the pokemons router component
            val component = setupComponent { createPokemonsComponent(it) }

            // Wait for the initial loading to complete
            advanceUntilIdle()

            // Click a pokemon in the list
            val listChild = component.childStack.value.active.instance as PokemonsComponent.Child.List
            listChild.component.onPokemonClick(TestPokemons.firePokemons.first().id)

            // Deliver router events
            runCurrent()

            // Verify details screen is shown
            component.childStack.value.active.instance.shouldBeInstanceOf<PokemonsComponent.Child.Details>()
        }
    }
})
