package ru.mobileup.template.features.root.ui

import com.arkivanov.decompose.router.RouterState
import ru.mobileup.template.core.message.ui.MessageComponent
import ru.mobileup.template.features.pokemons.ui.PokemonsComponent

interface RootComponent {

    val routerState: RouterState<*, Child>

    val messageComponent: MessageComponent

    sealed interface Child {
        class Pokemons(val component: PokemonsComponent) : Child
    }
}