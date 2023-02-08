package ru.mobileup.template.features.pokemons

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.algebra.withKey
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.network.NetworkApiFactory
import ru.mobileup.template.features.pokemons.data.PokemonApi
import ru.mobileup.template.features.pokemons.data.PokemonRepository
import ru.mobileup.template.features.pokemons.data.PokemonRepositoryImpl
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.ui.PokemonsComponent
import ru.mobileup.template.features.pokemons.ui.RealPokemonsComponent
import ru.mobileup.template.features.pokemons.ui.details.PokemonDetailsComponent
import ru.mobileup.template.features.pokemons.ui.details.RealPokemonDetailsComponent
import ru.mobileup.template.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.features.pokemons.ui.list.RealPokemonListComponent

val pokemonsModule = module {
    single<PokemonApi> { get<NetworkApiFactory>().unauthorizedKtorfit.create() }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
}

fun ComponentFactory.createPokemonsComponent(
    componentContext: ComponentContext
): PokemonsComponent {
    return RealPokemonsComponent(componentContext, get())
}

fun ComponentFactory.createPokemonListComponent(
    componentContext: ComponentContext,
    onOutput: (PokemonListComponent.Output) -> Unit
): PokemonListComponent {
    val pokemonsByTypeReplica = get<PokemonRepository>().pokemonsByTypeReplica
    return RealPokemonListComponent(componentContext, onOutput, pokemonsByTypeReplica, get())
}

fun ComponentFactory.createPokemonDetailsComponent(
    componentContext: ComponentContext,
    pokemonId: PokemonId
): PokemonDetailsComponent {
    val pokemonReplica = get<PokemonRepository>().pokemonByIdReplica.withKey(pokemonId)
    return RealPokemonDetailsComponent(componentContext, pokemonReplica, get())
}
