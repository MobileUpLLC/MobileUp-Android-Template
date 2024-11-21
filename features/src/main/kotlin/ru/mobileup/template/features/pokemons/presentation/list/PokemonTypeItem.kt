package ru.mobileup.template.features.pokemons.presentation.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.theme.custom.CustomTheme
import ru.mobileup.template.features.pokemons.domain.PokemonType

@Composable
fun PokemonTypeItem(
    type: PokemonType,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        shape = RoundedCornerShape(48.dp),
        color = when (isSelected) {
            true -> CustomTheme.colors.button.primary
            else -> CustomTheme.colors.button.secondary
        },
        shadowElevation = 6.dp
    ) {
        Text(
            text = type.name,
            style = CustomTheme.typography.body.regular,
            color = if (isSelected) {
                CustomTheme.colors.text.invert
            } else {
                CustomTheme.colors.text.primary
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
private fun PokemonTypeItemPreview() {
    var isSelected by remember { mutableStateOf(false) }
    AppTheme {
        PokemonTypeItem(
            type = PokemonType.Fire,
            isSelected = isSelected,
            onClick = {
                isSelected = !isSelected
            }
        )
    }
}