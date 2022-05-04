package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.domain.Pokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @get:Rule
    val mockServerRule = MockServerRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `shows empty state when loaded data is empty`() {
        mockServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakeData.pokemonsEmptyResponse)
        )
        val koin = koinTestRule.testKoin {
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { sut.pokemonsState.loading }
        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        Assert.assertEquals(
            Loadable<List<Pokemon>>(loading = false, data = emptyList(), error = null),
            actualPokemonsState
        )
    }

    @Test
    fun `shows data when it is loaded`() {
        mockServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakeData.pokemonListResponse)
        )
        val koin = koinTestRule.testKoin {
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        val data = listOf(
            Pokemon(
                id = PokemonId("4"),
                name = "Charmander"
            ),
            Pokemon(
                id = PokemonId("5"),
                name = "Charmeleon"
            ),
            Pokemon(
                id = PokemonId("6"),
                name = "Charizard"
            )
        )
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { sut.pokemonsState.loading }
        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        Assert.assertEquals(
            Loadable(loading = false, data = data, error = null),
            actualPokemonsState
        )
    }
}