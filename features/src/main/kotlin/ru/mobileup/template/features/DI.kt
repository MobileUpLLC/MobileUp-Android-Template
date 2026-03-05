package ru.mobileup.template.features

import org.koin.core.module.Module
import ru.mobileup.template.features.pokemons.pokemonsModule

val featureModules: List<Module> = listOf(
    pokemonsModule
)
