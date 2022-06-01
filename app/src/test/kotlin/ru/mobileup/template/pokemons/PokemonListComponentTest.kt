package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import ru.mobileup.core.error_handling.ServerException
import ru.mobileup.features.pokemons.createPokemonListComponent
import ru.mobileup.features.pokemons.ui.list.PokemonListComponent
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `loads fire pokemons initially`() {
        val koin = koinTestRule.testKoin()
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
    fun `redirects to details when a pokemon is clicked`() {
        val koin = koinTestRule.testKoin()
        var actualOutput: PokemonListComponent.Output? = null
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {
                actualOutput = it
            }
        componentContext.moveToState(Lifecycle.State.RESUMED)

        sut.onPokemonClick(FakePokemons.detailedPonyta.id)

        Assert.assertEquals(
            PokemonListComponent.Output.PokemonDetailsRequested(FakePokemons.detailedPonyta.id),
            actualOutput
        )
    }

    @Test
    fun `shows error when loading fire pokemons failed`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = true)
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

    @Test
    fun `update fire pokemons when retry click after failed loading`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = true)
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonListComponent(componentContext) {}
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = false)

        sut.onRetryClick()
        awaitUntil { !sut.pokemonsState.loading }
        val actualPokemonsState = sut.pokemonsState

        Assert.assertEquals(
            Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actualPokemonsState
        )
    }
}