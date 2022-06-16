package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import ru.mobileup.template.core.error_handling.ServerException
import ru.mobileup.template.features.pokemons.createPokemonListComponent
import ru.mobileup.template.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.utils.*
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `loads pokemons for the first tab on start`() {
        val koin = koinTestRule.testKoin()
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepareResponse("/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }

    @Test
    fun `redirects to details when a pokemon is clicked`() {
        val koin = koinTestRule.testKoin()
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val outputCaptor = OutputCaptor<PokemonListComponent.Output>()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext, outputCaptor)

        fakeWebServer.prepareResponse("/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        sut.onPokemonClick(FakePokemons.detailedPonyta.id)

        assertEquals(
            expected = listOf(PokemonListComponent.Output.PokemonDetailsRequested(FakePokemons.detailedPonyta.id)),
            actual = outputCaptor.outputs
        )
    }

    @Test
    fun `shows fullscreen error when pokemons loading failed`() {
        val koin = koinTestRule.testKoin()
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepareResponse("/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertTrue(sut.pokemonsState.error?.exception is ServerException)
    }

    @Test
    fun `update pokemons when retry is clicked after failed loading`() {
        val koin = koinTestRule.testKoin()
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepareResponse("/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }
        fakeWebServer.prepareResponse("/api/v2/type/10", FakePokemons.firePokemonsJson)
        sut.onRetryClick()
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }
}