package ru.mobileup.template.core.utils

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.backhandler.BackHandler

/**
 * Should be implemented by components with ChildStack to support predictive back animation
 */
interface PredictiveBackComponent : BackHandlerOwner {
    fun onBack()
}

/**
 * Creates a stack animation with predictive back support for [PredictiveBackComponent].
 */
@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun <C : Any, T : Any> PredictiveBackComponent.predictiveBackAnimation(): StackAnimation<C, T> {
    return createPlatformPredictiveBackAnimation(backHandler, ::onBack)
}

@ExperimentalDecomposeApi
internal expect fun <C : Any, T : Any> createPlatformPredictiveBackAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit
): StackAnimation<C, T>
