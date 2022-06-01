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
import ru.mobileup.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.utils.*

@RunWith(AndroidJUnit4::class)
class PokemonDetailsComponentTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `loads Ponyata details initially`() {
        val koin = koinTestRule.testKoin()
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, FakePokemons.detailedPonyta.id)
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonState.loading }
        val actualPokemonState = sut.pokemonState

        Assert.assertEquals(
            Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actualPokemonState
        )
    }

    @Test
    fun `shows error when loading Ponyata details failed`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = true)
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, FakePokemons.detailedPonyta.id)
        componentContext.moveToState(Lifecycle.State.RESUMED)

        awaitUntil { !sut.pokemonState.loading }
        val actualErrorPokemonState = sut.pokemonState

        Assert.assertNotNull(actualErrorPokemonState.error)
        Assert.assertEquals(1, actualErrorPokemonState.error?.exceptions?.count())
        Assert.assertTrue(actualErrorPokemonState.error?.exceptions?.first() is ServerException)
    }

    @Test
    fun `update Ponyata details when retry click after failed loading`() {
        val koin = koinTestRule.testKoin()
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = true)
        val componentContext = TestComponentContext()
        val sut = koin
            .componentFactory
            .createPokemonDetailsComponent(componentContext, FakePokemons.detailedPonyta.id)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }
        koin.get<FakeWebServer>().changeDispatcherConfiguration(hasBrokenResponse = false)

        sut.onRetryClick()
        awaitUntil { !sut.pokemonState.loading }
        val actualPokemonState = sut.pokemonState

        Assert.assertEquals(
            Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actualPokemonState
        )
    }
}