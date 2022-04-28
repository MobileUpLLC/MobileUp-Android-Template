package ru.mobileup.template.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.*

class TestComponentContext(
    override val lifecycle: LifecycleRegistry = LifecycleRegistry()
) : ComponentContext by DefaultComponentContext(lifecycle) {

    fun moveToState(lifecycleState: Lifecycle.State) {
        when (lifecycleState) {
            Lifecycle.State.INITIALIZED -> Unit
            Lifecycle.State.CREATED -> lifecycle.create()
            Lifecycle.State.STARTED -> lifecycle.start()
            Lifecycle.State.RESUMED -> lifecycle.resume()
            Lifecycle.State.DESTROYED -> lifecycle.destroy()
        }
    }
}