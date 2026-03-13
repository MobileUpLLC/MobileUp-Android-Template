package ru.mobileup.template.features.root

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import ru.mobileup.template.features.integrationTest
import ru.mobileup.template.features.root.presentation.RootComponent

class RootComponentTest : FunSpec({

    context("Root component") {

        integrationTest("shows pokemons as initial screen") {
            val component = setupComponent { createRootComponent(it) }

            component.childStack.value.active.instance.shouldBeInstanceOf<RootComponent.Child.Pokemons>()
        }

        integrationTest("creates message component") {
            val component = setupComponent { createRootComponent(it) }

            component.messageComponent.shouldNotBeNull()
        }
    }
})