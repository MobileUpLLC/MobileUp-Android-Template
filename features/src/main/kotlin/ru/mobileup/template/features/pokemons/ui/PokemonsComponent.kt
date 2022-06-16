package ru.mobileup.template.features.pokemons.ui

import com.arkivanov.decompose.router.RouterState
import ru.mobileup.template.features.pokemons.ui.details.PokemonDetailsComponent
import ru.mobileup.template.features.pokemons.ui.list.PokemonListComponent

interface PokemonsComponent {

    val routerState: RouterState<*, Child>

    sealed interface Child {
        class List(val component: PokemonListComponent) : Child
        class Details(val component: PokemonDetailsComponent) : Child
    }
}