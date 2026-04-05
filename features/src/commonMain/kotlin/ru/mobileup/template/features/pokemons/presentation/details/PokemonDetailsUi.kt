package ru.mobileup.template.features.pokemons.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.stringResource
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.theme.custom.CustomTheme
import ru.mobileup.template.core.utils.LocalBackAction
import ru.mobileup.template.core.widget.PullRefreshLceWidget
import ru.mobileup.template.core.widget.RefreshingProgress
import ru.mobileup.template.features.generated.resources.Res
import ru.mobileup.template.features.generated.resources.pokemons_height
import ru.mobileup.template.features.generated.resources.pokemons_types
import ru.mobileup.template.features.generated.resources.pokemons_weight
import ru.mobileup.template.features.pokemons.domain.DetailedPokemon
import ru.mobileup.template.features.pokemons.presentation.list.PokemonTypeItem

@Composable
fun PokemonDetailsUi(
    component: PokemonDetailsComponent,
    modifier: Modifier = Modifier
) {
    val pokemonState by component.pokemonState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PokemonNameToolbar(
                name = pokemonState.data?.name
            )
        }
    ) { innerPadding ->
        PullRefreshLceWidget(
            state = pokemonState,
            innerPadding = innerPadding,
            onRefresh = component::onRefresh
        ) { pokemon, refreshing, contentPadding ->
            PokemonDetailsContent(
                pokemon = pokemon,
                contentPadding = contentPadding
            )
            RefreshingProgress(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(top = 4.dp),
                active = refreshing
            )
        }
    }
}

@Composable
private fun PokemonNameToolbar(
    name: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CustomTheme.colors.background.screen,
        shadowElevation = 4.dp
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            IconButton(onClick = LocalBackAction.current) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 64.dp, vertical = 4.dp)
                    .align(Alignment.Center),
                text = name.orEmpty(),
                style = CustomTheme.typography.title.regular
            )
        }
    }
}

@Composable
private fun PokemonDetailsContent(
    pokemon: DetailedPokemon,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(pokemon.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 32.dp)
                .size(200.dp)
                .clip(CircleShape)
                .background(color = CustomTheme.colors.background.screen)
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(Res.string.pokemons_types)
        )

        Row(
            modifier = Modifier
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            pokemon.types.forEach {
                PokemonTypeItem(type = it, isSelected = true)
            }
        }

        Row(
            modifier = Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.pokemons_height, pokemon.height)
            )
            Text(
                text = stringResource(Res.string.pokemons_weight, pokemon.weight)
            )
        }
    }
}

@Preview
@Composable
private fun PokemonDetailsUiPreview() {
    AppTheme {
        PokemonDetailsUi(FakePokemonDetailsComponent())
    }
}
