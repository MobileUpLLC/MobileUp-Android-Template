package ru.mobileup.template.core.utils

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import ru.mobileup.template.core.LocalPlatformUiProvider

/**
 * Should be implemented by components with ChildStack to support predictive back animation
 */
interface PredictiveBackComponent : BackHandlerOwner {
    fun onBack()
}

/**
 * A convenient method to create [com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation]
 * for components implementing [PredictiveBackComponent].
 */
@Composable
fun <C : Any, T : Any> PredictiveBackComponent.predictiveBackAnimation(): StackAnimation<C, T> {
    val uiProvider = LocalPlatformUiProvider.current
    return uiProvider.createPredictiveBackAnimation(backHandler, ::onBack)
}
