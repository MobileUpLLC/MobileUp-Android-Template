package ru.mobileup.template.utils

import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
abstract class ComponentSpec(
    body: BehaviorSpec.() -> Unit
) : BehaviorSpec(body) {

    private val testDispatcher = UnconfinedTestDispatcher()

    init {
        beforeTest {
            Dispatchers.setMain(testDispatcher)
        }

        afterTest {
            Dispatchers.resetMain()
        }
    }
}
