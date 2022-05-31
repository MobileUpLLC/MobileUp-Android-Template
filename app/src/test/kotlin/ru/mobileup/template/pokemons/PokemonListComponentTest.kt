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
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.domain.PokemonId
import ru.mobileup.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create()


    @Test
    fun `shows data when it is loaded`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().sendResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakePokemons.firePokemonsJson)
        )
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        Assert.assertEquals(
            Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actualPokemonsState
        )
    }

    @Test
    fun `sends output when pokemon click`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().sendResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(FakePokemons.firePokemonsJson)
        )
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
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().sendResponse(MockResponse().setResponseCode(404))
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