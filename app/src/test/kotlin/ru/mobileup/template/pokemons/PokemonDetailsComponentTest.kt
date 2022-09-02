package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import org.junit.Test
import org.junit.runner.RunWith
import ru.mobileup.template.core.error_handling.ServerException
import ru.mobileup.template.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.utils.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class PokemonDetailsComponentTest {

    @Test
    fun `loads pokemon details for a specified id on start`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        fakeWebServer.prepareResponse("/api/v2/pokemon/77", FakePokemons.detailedPonytaJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actual = sut.pokemonState
        )
    }

    @Test
    fun `shows fullscreen error when details loading failed`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        fakeWebServer.prepareResponse("/api/v2/pokemon/77", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }

        assertTrue(sut.pokemonState.error?.exception is ServerException)
    }

    @Test
    fun `reloads details when retry is clicked after failed loading`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        fakeWebServer.prepareResponse("/api/v2/pokemon/77", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }
        fakeWebServer.prepareResponse("/api/v2/pokemon/77", FakePokemons.detailedPonytaJson)
        sut.onRetryClick()
        awaitUntil { !sut.pokemonState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actual = sut.pokemonState
        )
    }
}