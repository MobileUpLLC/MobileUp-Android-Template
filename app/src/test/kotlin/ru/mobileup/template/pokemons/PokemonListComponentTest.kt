package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import ru.mobileup.template.core.error_handling.ServerException
import ru.mobileup.template.features.pokemons.createPokemonListComponent
import ru.mobileup.template.features.pokemons.domain.PokemonTypeId
import ru.mobileup.template.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.utils.*
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @Test
    fun `loads pokemons for the first tab on start`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }

    @Test
    fun `loads pokemons for the second tab after tab is changed`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepareResponse("/api/v2/type/10", FakePokemons.firePokemonsJson)
        fakeWebServer.prepareResponse("/api/v2/type/11", FakePokemons.watterPokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }
        sut.onTypeClick(PokemonTypeId("11"))
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.watterPokemons, error = null),
            actual = sut.pokemonsState
        )
    }

    @Test
    fun `redirects to details when a pokemon is clicked`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val outputCaptor = OutputCaptor<PokemonListComponent.Output>()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext, outputCaptor)

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        sut.onPokemonClick(FakePokemons.detailedPonyta.id)

        assertEquals(
            expected = listOf(PokemonListComponent.Output.PokemonDetailsRequested(FakePokemons.detailedPonyta.id)),
            actual = outputCaptor.outputs
        )
    }

    @Test
    fun `shows fullscreen error when pokemons loading failed`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertTrue(sut.pokemonsState.error?.exception is ServerException)
    }

    @Test
    fun `update pokemons when retry is clicked after failed loading`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }
        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        sut.onRetryClick()
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }
}