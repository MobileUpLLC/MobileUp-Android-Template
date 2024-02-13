package ru.mobileup.template.features.pokemons.presentation.details

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.desc
import me.aartikov.replica.algebra.normal.withKey
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message
import ru.mobileup.template.core.utils.observe
import ru.mobileup.template.features.pokemons.data.PokemonRepository
import ru.mobileup.template.features.pokemons.domain.PokemonId
import ru.mobileup.template.features.pokemons.domain.PokemonType

class RealPokemonDetailsComponent(
    componentContext: ComponentContext,
    pokemonId: PokemonId,
    pokemonRepository: PokemonRepository,
    private val messageService: MessageService,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, PokemonDetailsComponent {

    private val pokemonReplica = pokemonRepository.pokemonByIdReplica.withKey(pokemonId)

    override val pokemonState = pokemonReplica.observe(this, errorHandler)

    override fun onTypeClick(type: PokemonType) {
        pokemonState.value.data?.let {
            messageService.showMessage(
                Message(
                    text = "Types are: ${it.types.joinToString { it.name }}".desc()
                )
            )
        }
    }

    override fun onRetryClick() {
        pokemonReplica.refresh()
    }

    override fun onRefresh() {
        pokemonReplica.refresh()
    }
}
