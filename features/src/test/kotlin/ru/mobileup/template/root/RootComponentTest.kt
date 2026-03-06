package ru.mobileup.template.root

import io.kotest.matchers.nulls.shouldNotBeNull
import ru.mobileup.template.core.utils.getChild
import ru.mobileup.template.features.root.createRootComponent
import ru.mobileup.template.features.root.presentation.RootComponent
import ru.mobileup.template.utils.ComponentFixture
import ru.mobileup.template.utils.ComponentSpec
import ru.mobileup.template.utils.componentFactory
import ru.mobileup.template.utils.createComponentFixture
import ru.mobileup.template.utils.registerFixture

class RootComponentTest : ComponentSpec({

    Given("root component") {

        val fixture: () -> ComponentFixture<RootComponent, Nothing> = registerFixture {
            createComponentFixture { koin, componentContext ->
                koin.componentFactory.createRootComponent(componentContext)
            }
        }

        When("component is created") {
            then("it has pokemons child as initial screen") {
                fixture().sut.childStack.value
                    .getChild<RootComponent.Child.Pokemons>()
                    .shouldNotBeNull()
            }
        }

        When("component is created") {
            then("it creates message component") {
                fixture().sut.messageComponent.shouldNotBeNull()
            }
        }
    }
})

