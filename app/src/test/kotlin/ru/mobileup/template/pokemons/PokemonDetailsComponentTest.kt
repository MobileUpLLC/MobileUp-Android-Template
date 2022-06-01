package ru.mobileup.template.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule
import ru.mobileup.core.error_handling.ServerException
import ru.mobileup.features.pokemons.createPokemonDetailsComponent
import ru.mobileup.template.utils.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class PokemonDetailsComponentTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create()

    @Test
    fun `loads pokemon details for a specified id on start`() {
        val koin = koinTestRule.testKoin()
        koin.fakeWebServer.setDispatcher(
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when (request.requestUrl?.encodedPath) {
                        "/api/v2/pokemon/77" -> {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(FakePokemons.detailedPonytaJson)
                        }

                        else -> throw IllegalArgumentException("Unexpected request: $request")
                    }
                }
            }
        )
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actual = sut.pokemonState
        )
    }

    @Test
    fun `shows fullscreen error when details loading failed`() {
        val koin = koinTestRule.testKoin()
        koin.fakeWebServer.setDispatcher(
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when (request.requestUrl?.encodedPath) {
                        "/api/v2/pokemon/77" -> MockResponse().setResponseCode(404)
                        else -> throw IllegalArgumentException("Unexpected request: $request")
                    }
                }
            }
        )
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }

        assertTrue(sut.pokemonState.error?.exception is ServerException)
    }

    @Test
    fun `reloads details when retry is clicked after failed loading`() {
        val koin = koinTestRule.testKoin()
        koin.fakeWebServer.setDispatcher(
            object : Dispatcher() {
                var isFirstResponse = true
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return if (request.requestUrl?.encodedPath == "/api/v2/pokemon/77" && isFirstResponse) {
                        isFirstResponse = false
                        MockResponse().setResponseCode(404)
                    } else {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(FakePokemons.detailedPonytaJson)
                    }
                }
            }
        )
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonDetailsComponent(
            componentContext,
            FakePokemons.detailedPonyta.id
        )

        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonState.loading }
        sut.onRetryClick()
        awaitUntil { !sut.pokemonState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.detailedPonyta, error = null),
            actual = sut.pokemonState
        )
    }
}