package ru.mobileup.template.features.pokemons.ui.details

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.desc
import me.aartikov.replica.single.Replica
import ru.mobileup.template.core.error_handling.ErrorHandler
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.Message
import ru.mobileup.template.core.utils.observe
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.domain.PokemonType

class RealPokemonDetailsComponent(
    componentContext: ComponentContext,
    private val pokemonReplica: Replica<DetailedPokemon>,
    private val messageService: MessageService,
    errorHandler: ErrorHandler
) : ComponentContext by componentContext, PokemonDetailsComponent {

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
