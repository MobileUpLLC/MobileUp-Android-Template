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
import org.robolectric.annotation.Config
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.domain.Pokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.domain.PokemonTypeId
import ru.mobileup.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
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

    @Test
    fun `update data when retry click`() {
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
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { sut.pokemonsState.loading }
        awaitUntil { !sut.pokemonsState.loading }

        sut.onRetryClick()
        mockServerRule.server.enqueue(MockResponse().setResponseCode(404))
        awaitUntil { sut.pokemonsState.loading }
        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        Assert.assertNotNull(actualPokemonsState.error)
        Assert.assertEquals(1, actualPokemonsState.error?.exceptions?.count())
    }

    @Test
    fun `shows error when loader failed`() {
        mockServerRule.server.enqueue(MockResponse().setResponseCode(404))
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

        Assert.assertNotNull(actualPokemonsState.error)
        Assert.assertEquals(1, actualPokemonsState.error?.exceptions?.count())
    }

    @Test
    fun `sends output when pokemon click`() {
        val koin = koinTestRule.testKoin()
        var actualOutput: PokemonListComponent.Output? = null
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {
                actualOutput = it
            }
        componentContext.moveToState(Lifecycle.State.RESUMED)

        sut.onPokemonClick(PokemonId("1"))

        Assert.assertEquals(
            PokemonListComponent.Output.PokemonDetailsRequested(PokemonId("1")),
            actualOutput
        )
    }

    @Test
    fun `sends output when type click`() {
        val koin = koinTestRule.testKoin()
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

        sut.onTypeClick(PokemonTypeId("101"))
        val actualSelectedTypeId = sut.selectedTypeId

        Assert.assertEquals(PokemonTypeId("101"), actualSelectedTypeId)
    }
}