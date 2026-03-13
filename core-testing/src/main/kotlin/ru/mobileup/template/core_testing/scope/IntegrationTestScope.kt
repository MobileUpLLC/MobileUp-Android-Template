package ru.mobileup.template.core_testing.scope

import com.arkivanov.essenty.lifecycle.Lifecycle
import io.kotest.core.test.TestScope
import ru.mobileup.template.core_testing.message.data.TestMessageService
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core_testing.network.MockServer
import ru.mobileup.template.core_testing.utils.TestComponentContext
import kotlin.time.Duration

interface IntegrationTestScope : TestScope {
    val mockServer: MockServer
    val testMessageService: TestMessageService

    // Методы управления временем остаются
    fun advanceUntilIdle()
    fun advanceTimeBy(delayTime: Duration)
    fun runCurrent()

    fun <T> setupComponent(
        targetState: Lifecycle.State = Lifecycle.State.RESUMED,
        create: ComponentFactory.(TestComponentContext) -> T
    ): T

    fun <T> setupComponentWithContext(
        targetState: Lifecycle.State = Lifecycle.State.RESUMED,
        create: ComponentFactory.(TestComponentContext) -> T
    ): Pair<T, TestComponentContext>
}
