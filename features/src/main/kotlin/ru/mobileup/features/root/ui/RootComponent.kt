package ru.mobileup.features.root.ui

import com.arkivanov.decompose.router.RouterState
import ru.mobileup.core.message.ui.MessageComponent
import ru.mobileup.features.pokemons.ui.PokemonsComponent

interface RootComponent {

    val routerState: RouterState<*, Child>

    val messageComponent: MessageComponent

    sealed interface Child {
        class Pokemons(val component: PokemonsComponent) : Child
    }
}