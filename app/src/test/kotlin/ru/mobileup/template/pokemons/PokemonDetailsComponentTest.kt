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
import ru.mobileup.core.error_handling.ServerException
import ru.mobileup.core.network.BaseUrlProvider
import ru.mobileup.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.features.pokemons.domain.DetailedPokemon
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.domain.PokemonType
import ru.mobileup.features.pokemons.domain.PokemonTypeId
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
class PokemonDetailsComponentTest {

    @get:Rule
    val mockServerRule = MockServerRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `shows data when it is loaded`() {
        mockServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakeData.detailedPokemonResponse)
        )
        val koin = koinTestRule.testKoin {
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val pokemonId = PokemonId("77")
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, pokemonId = pokemonId)
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonState.loading }
        val actualPokemonState = sut.pokemonState

        val expectedData = DetailedPokemon(
            id = pokemonId,
            name = "Ponyta",
            height = 1f,
            weight = 30f,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/77.png",
            types = listOf(PokemonType(id = PokemonTypeId("10"), name = "Fire"))
        )
        Assert.assertEquals(
            Loadable(loading = false, data = expectedData, error = null),
            actualPokemonState
        )
    }

    @Test
    fun `shows error when loading failed`() {
        mockServerRule.server.enqueue(MockResponse().setResponseCode(404))
        val koin = koinTestRule.testKoin {
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, pokemonId = PokemonId("77"))
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonState.loading }
        val actualErrorPokemonState = sut.pokemonState

        Assert.assertNotNull(actualErrorPokemonState.error)
        Assert.assertEquals(1, actualErrorPokemonState.error?.exceptions?.count())
        Assert.assertTrue(actualErrorPokemonState.error?.exceptions?.first() is ServerException)
    }

    @Test
    fun `update data when retry click`() {
        mockServerRule.server.enqueue(MockResponse().setResponseCode(404))
        mockServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakeData.detailedPokemonResponse)
        )
        val koin = koinTestRule.testKoin {
            single<BaseUrlProvider> { MockServerBaseUrlProvider(mockServerRule) }
        }
        val componentContext = TestComponentContext()
        val pokemonId = PokemonId("77")
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, pokemonId = pokemonId)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }

        sut.onRetryClick()
        awaitUntil { !sut.pokemonState.loading }
        val actualPokemonState = sut.pokemonState

        val expectedData = DetailedPokemon(
            id = pokemonId,
            name = "Ponyta",
            height = 1f,
            weight = 30f,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/77.png",
            types = listOf(PokemonType(id = PokemonTypeId("10"), name = "Fire"))
        )
        Assert.assertEquals(
            Loadable(loading = false, data = expectedData, error = null),
            actualPokemonState
        )
    }
}