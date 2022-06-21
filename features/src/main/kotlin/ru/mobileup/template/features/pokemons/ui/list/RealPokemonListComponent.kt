package ru.mobileup.template.features.pokemons.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import me.aartikov.replica.keyed.KeyedReplica
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.utils.observe
import ru.mobileup.template.features.pokemons.domain.Pokemon
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType
import ru.mobileup.template.features.pokemons.domain.PokemonTypeId

class RealPokemonListComponent(
    componentContext: ComponentContext,
    private val onOutput: (PokemonListComponent.Output) -> Unit,
    private val pokemonsByTypeReplica: KeyedReplica<PokemonTypeId, List<Pokemon>>,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, PokemonListComponent {

    override val types = listOf(
        PokemonType.Fire,
        PokemonType.Water,
        PokemonType.Electric,
        PokemonType.Grass,
        PokemonType.Poison
    )

    override var selectedTypeId by mutableStateOf(
        stateKeeper.consume("selectedTypeId") ?: types[0].id
    )
        private set

    override val pokemonsState by pokemonsByTypeReplica.observe(
        lifecycle,
        errorHandler,
        key = { selectedTypeId },
        keepPreviousData = true
    )

    init {
        stateKeeper.register("selectedTypeId") { selectedTypeId }
    }

    override fun onTypeClick(typeId: PokemonTypeId) {
        selectedTypeId = typeId
    }

    override fun onPokemonClick(pokemonId: PokemonId) {
        onOutput(PokemonListComponent.Output.PokemonDetailsRequested(pokemonId))
    }

    override fun onRetryClick() {
        pokemonsByTypeReplica.refresh(selectedTypeId)
    }

    override fun onRefresh() {
        pokemonsByTypeReplica.refresh(selectedTypeId)
    }
}