package ru.mobileup.template.features.pokemons.presentation

import ru.mobileup.template.core.utils.createFakeChildStackStateFlow
import ru.mobileup.template.features.pokemons.presentation.list.FakePokemonListComponent

class FakePokemonsComponent : PokemonsComponent {

    override val childStack = createFakeChildStackStateFlow(
        PokemonsComponent.Child.List(FakePokemonListComponent())
    )
}
