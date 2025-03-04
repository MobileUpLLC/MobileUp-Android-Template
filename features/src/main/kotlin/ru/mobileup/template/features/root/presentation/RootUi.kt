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
import ru.mobileup.template.core.utils.ConfigureSystemBars
import ru.mobileup.template.core.utils.LocalSystemBarsSettings
import ru.mobileup.template.core.utils.accumulate
import ru.mobileup.template.features.pokemons.presentation.PokemonsUi

@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Pokemons -> PokemonsUi(instance.component)
        }
    }

    MessageUi(
        component = component.messageComponent,
        bottomPadding = 16.dp
    )

    SystemBarsColors()
}

@Composable
private fun SystemBarsColors() {
    val systemBarsSettings = LocalSystemBarsSettings.current.accumulate()
    ConfigureSystemBars(systemBarsSettings)
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}