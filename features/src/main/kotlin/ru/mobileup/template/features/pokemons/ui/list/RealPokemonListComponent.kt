package ru.mobileup.template.features.pokemons.ui.list

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize
import me.aartikov.replica.keyed.KeyedPhysicalReplica
import me.aartikov.replica.keyed.KeyedReplica
import me.aartikov.replica.keyed.keepPreviousData
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.utils.observe
import ru.mobileup.template.core.utils.persistent
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

    private val selectedTypeIdStateFlow = MutableStateFlow(types[0].id)

    override var selectedTypeId by selectedTypeIdStateFlow::value

    override val pokemonsState by pokemonsByTypeReplica
        .keepPreviousData()
        .observe(
            lifecycle,
            errorHandler,
            selectedTypeIdStateFlow
        )

    override val replica: KeyedPhysicalReplica<PokemonTypeId, List<Pokemon>>
        get() = pokemonsByTypeReplica as KeyedPhysicalReplica<PokemonTypeId, List<Pokemon>>

    init {
        persistent(
            save = { PersistentState(selectedTypeId) },
            restore = { state -> selectedTypeId = state.selectedTypeId }
        )
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

    @Parcelize
    private data class PersistentState(
        val selectedTypeId: PokemonTypeId
    ) : Parcelable
}