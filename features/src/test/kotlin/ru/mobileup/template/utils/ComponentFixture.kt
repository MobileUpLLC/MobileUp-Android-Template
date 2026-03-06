package ru.mobileup.template.utils

import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import org.koin.core.Koin

class ComponentFixture<T : Any, O>(
    val koin: Koin,
    val componentContext: TestComponentContext,
    createSut: () -> T,
    private val outputProvider: () -> O?,
    private val closeAction: () -> Unit
) {
    val sut: T by lazy(LazyThreadSafetyMode.NONE) { createSut() }

    val output: O?
        get() = outputProvider()

    fun close() {
        closeAction()
    }
}

fun <T : Any> createComponentFixture(
    createComponent: (koin: Koin, componentContext: TestComponentContext) -> T
): ComponentFixture<T, Nothing> {
    val testKoin = createTestKoin()
    val koin = testKoin.koin
    val componentContext = TestComponentContext()

    return ComponentFixture(
        koin = koin,
        componentContext = componentContext,
        createSut = { createComponent(koin, componentContext) },
        outputProvider = { null },
        closeAction = { testKoin.close() }
    )
}

fun <T : Any, O> createComponentFixtureWithOutput(
    createComponent: (
        koin: Koin,
        componentContext: TestComponentContext,
        onOutput: (O) -> Unit
    ) -> T
): ComponentFixture<T, O> {
    val testKoin = createTestKoin()
    val koin = testKoin.koin
    val componentContext = TestComponentContext()
    var output: O? = null

    return ComponentFixture(
        koin = koin,
        componentContext = componentContext,
        createSut = { createComponent(koin, componentContext) { output = it } },
        outputProvider = { output },
        closeAction = { testKoin.close() }
    )
}

fun <T : Any, O> BehaviorSpecGivenContainerScope.registerFixture(
    createFixture: () -> ComponentFixture<T, O>
): () -> ComponentFixture<T, O> {
    var fixture: ComponentFixture<T, O>? = null

    beforeTest {
        fixture = createFixture()
    }

    afterTest {
        fixture?.close()
        fixture = null
    }

    return { fixture ?: error("Fixture is not initialized yet") }
}
