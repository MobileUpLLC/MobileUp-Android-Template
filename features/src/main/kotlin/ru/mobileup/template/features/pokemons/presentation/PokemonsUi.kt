package ru.mobileup.template.features.pokemons.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.features.pokemons.presentation.details.PokemonDetailsUi
import ru.mobileup.template.features.pokemons.presentation.list.PokemonListUi

@Composable
fun PokemonsUi(
    component: PokemonsComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is PokemonsComponent.Child.List -> PokemonListUi(instance.component)
            is PokemonsComponent.Child.Details -> PokemonDetailsUi(instance.component)
        }
    }
}

@Preview
@Composable
private fun PokemonsUiPreview() {
    AppTheme {
        PokemonsUi(FakePokemonsComponent())
    }
}