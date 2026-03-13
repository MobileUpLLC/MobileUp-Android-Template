package ru.mobileup.template.core_testing.scope

import com.arkivanov.essenty.lifecycle.Lifecycle
import io.kotest.core.test.TestScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.koin.core.Koin
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core_testing.message.data.TestMessageService
import ru.mobileup.template.core_testing.network.MockServer
import ru.mobileup.template.core_testing.utils.TestComponentContext
import kotlin.time.Duration

class IntegrationTestScopeImpl(
    koin: Koin,
    private val kotestScope: TestScope,
    private val testScheduler: TestCoroutineScheduler
) : IntegrationTestScope, TestScope by kotestScope {

    override val mockServer: MockServer = koin.get()
    override val testMessageService: TestMessageService = koin.get<MessageService>() as TestMessageService
    private val componentFactory: ComponentFactory = koin.get()

    override fun advanceUntilIdle() = testScheduler.advanceUntilIdle()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun advanceTimeBy(delayTime: Duration) = testScheduler.advanceTimeBy(delayTime.inWholeMilliseconds)

    override fun runCurrent() = testScheduler.runCurrent()

    override fun <T> setupComponent(
        targetState: Lifecycle.State,
        create: ComponentFactory.(TestComponentContext) -> T
    ): T {
        return setupComponentWithContext(targetState, create).first
    }

    override fun <T> setupComponentWithContext(
        targetState: Lifecycle.State,
        create: ComponentFactory.(TestComponentContext) -> T
    ): Pair<T, TestComponentContext> {
        val lifecycle = TestComponentContext()
        val component = componentFactory.create(lifecycle)
        lifecycle.moveToState(targetState)

        return component to lifecycle
    }
}
