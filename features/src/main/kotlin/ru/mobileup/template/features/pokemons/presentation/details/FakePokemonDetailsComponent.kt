package ru.mobileup.template.features.pokemons.presentation.details

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.template.core.utils.LoadableState
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType

class FakePokemonDetailsComponent : PokemonDetailsComponent {

    override val pokemonState = MutableStateFlow(
        LoadableState(
            loading = true,
            data = DetailedPokemon(
                id = PokemonId("1"),
                name = "Bulbasaur",
                imageUrl = "",
                height = 0.7f,
                weight = 6.9f,
                types = listOf(PokemonType.Grass, PokemonType.Poison)
            )
        )
    )

    override fun onTypeClick(type: PokemonType) = Unit

    override fun onRetryClick() = Unit

    override fun onRefresh() = Unit
}
