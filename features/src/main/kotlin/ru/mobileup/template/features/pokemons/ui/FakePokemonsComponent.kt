package ru.mobileup.template.features.pokemons.ui

import ru.mobileup.template.core.utils.createFakeChildStackStateFlow
import ru.mobileup.template.features.pokemons.ui.list.FakePokemonListComponent

class FakePokemonsComponent : PokemonsComponent {

    override val childStack = createFakeChildStackStateFlow(
        PokemonsComponent.Child.List(FakePokemonListComponent())
    )
}
