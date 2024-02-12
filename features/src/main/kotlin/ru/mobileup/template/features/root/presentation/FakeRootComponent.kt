package ru.mobileup.template.features.root.presentation

import ru.mobileup.template.core.message.presentation.FakeMessageComponent
import ru.mobileup.template.core.utils.createFakeChildStackStateFlow
import ru.mobileup.template.features.pokemons.presentation.FakePokemonsComponent

class FakeRootComponent : RootComponent {

    override val childStack = createFakeChildStackStateFlow(
        RootComponent.Child.Pokemons(FakePokemonsComponent())
    )

    override val messageComponent = FakeMessageComponent()
}
