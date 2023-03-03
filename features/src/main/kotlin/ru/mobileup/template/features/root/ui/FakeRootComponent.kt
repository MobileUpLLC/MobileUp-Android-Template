package ru.mobileup.template.features.root.ui

import ru.mobileup.template.core.message.ui.FakeMessageComponent
import ru.mobileup.template.core.utils.createFakeChildStackStateFlow
import ru.mobileup.template.features.pokemons.ui.FakePokemonsComponent

class FakeRootComponent : RootComponent {

    override val childStack = createFakeChildStackStateFlow(
        RootComponent.Child.Pokemons(FakePokemonsComponent())
    )

    override val messageComponent = FakeMessageComponent()
}
