package ru.mobileup.template

import ru.mobileup.core.coreModule
import ru.mobileup.features.pokemons.pokemonsModule

val allModules = listOf(
    coreModule(BuildConfig.BACKEND_URL),
    pokemonsModule
)