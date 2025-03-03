package ru.mobileup.template.features.root.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.template.core.message.presentation.MessageUi
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.features.pokemons.presentation.PokemonsUi

@Suppress("ModifierReused")
@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Pokemons -> PokemonsUi(instance.component)
        }
    }

    MessageUi(
        component = component.messageComponent,
        modifier = modifier,
        bottomPadding = 16.dp
    )
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}