package ru.mobileup.template.features.pokemons.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.utils.createFakeChildStack
import ru.mobileup.template.features.pokemons.ui.details.PokemonDetailsUi
import ru.mobileup.template.features.pokemons.ui.list.FakePokemonListComponent
import ru.mobileup.template.features.pokemons.ui.list.PokemonListUi

@Composable
fun PokemonsUi(
    component: PokemonsComponent,
    modifier: Modifier = Modifier
) {
    Children(component.childStack, modifier) { child ->
        when (val instance = child.instance) {
            is PokemonsComponent.Child.List -> PokemonListUi(instance.component)
            is PokemonsComponent.Child.Details -> PokemonDetailsUi(instance.component)
        }
    }
}

@Preview
@Composable
fun PokemonsUiPreview() {
    AppTheme {
        PokemonsUi(FakePokemonsComponent())
    }
}

class FakePokemonsComponent : PokemonsComponent {

    override val childStack = createFakeChildStack(
        PokemonsComponent.Child.List(FakePokemonListComponent())
    )
}