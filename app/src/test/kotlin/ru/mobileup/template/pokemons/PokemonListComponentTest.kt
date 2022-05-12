package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.network.NetworkConnectivityProvider
import me.aartikov.replica.single.Loadable
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import org.robolectric.annotation.Config
import ru.mobileup.core.error_handling.ServerException
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.domain.Pokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.domain.PokemonTypeId
import ru.mobileup.features.pokemons.ui.list.PokemonListComponent
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
                .setBody(FakeData.pokemonListEmptyResponse)
        )
        val koin = koinTestRule.testKoin {
            single<NetworkConnectivityProvider> { FakeAndroidNetworkConnectivityProvider() }
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

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
            single<NetworkConnectivityProvider> { FakeAndroidNetworkConnectivityProvider() }
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        val expectedData = listOf(
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
        Assert.assertEquals(
            Loadable(loading = false, data = expectedData, error = null),
            actualPokemonsState
        )
    }

    @Test
    fun `sends output when pokemon click`() {
        mockServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakeData.pokemonListResponse)
        )
        val koin = koinTestRule.testKoin {
            single<NetworkConnectivityProvider> { FakeAndroidNetworkConnectivityProvider() }
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
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
    fun `shows error when loading failed`() {
        mockServerRule.server.enqueue(MockResponse().setResponseCode(404))
        val koin = koinTestRule.testKoin {
            single<NetworkConnectivityProvider> { FakeAndroidNetworkConnectivityProvider() }
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonsState.loading }
        val actualErrorPokemonsState = sut.pokemonsState

        Assert.assertNotNull(actualErrorPokemonsState.error)
        Assert.assertEquals(1, actualErrorPokemonsState.error?.exceptions?.count())
        Assert.assertTrue(actualErrorPokemonsState.error?.exceptions?.first() is ServerException)
    }
}