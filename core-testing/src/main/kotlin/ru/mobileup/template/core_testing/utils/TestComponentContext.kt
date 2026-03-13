package ru.mobileup.template.core_testing.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop

class TestComponentContext(
    override val lifecycle: LifecycleRegistry = LifecycleRegistry()
) : ComponentContext by DefaultComponentContext(lifecycle) {

    fun moveToState(lifecycleState: Lifecycle.State) {
        while (lifecycle.state != lifecycleState) {
            when (lifecycle.state) {
                Lifecycle.State.INITIALIZED -> lifecycle.create()
                Lifecycle.State.CREATED -> when (lifecycleState) {
                    Lifecycle.State.STARTED, Lifecycle.State.RESUMED -> lifecycle.start()
                    Lifecycle.State.DESTROYED -> lifecycle.destroy()
                    else -> return
                }

                Lifecycle.State.STARTED -> when (lifecycleState) {
                    Lifecycle.State.RESUMED -> lifecycle.resume()
                    Lifecycle.State.CREATED, Lifecycle.State.INITIALIZED -> lifecycle.stop()
                    Lifecycle.State.DESTROYED -> lifecycle.destroy()
                    else -> return
                }

                Lifecycle.State.RESUMED -> when (lifecycleState) {
                    Lifecycle.State.STARTED -> lifecycle.pause()
                    Lifecycle.State.CREATED, Lifecycle.State.INITIALIZED -> {
                        lifecycle.pause()
                        lifecycle.stop()
                    }

                    Lifecycle.State.DESTROYED -> lifecycle.destroy()
                    else -> return
                }

                Lifecycle.State.DESTROYED -> return
            }
        }
    }
}
