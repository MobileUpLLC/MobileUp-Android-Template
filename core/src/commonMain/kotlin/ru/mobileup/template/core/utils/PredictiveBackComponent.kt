package ru.mobileup.template.core.utils

import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandlerOwner

/**
 * Should be implemented by components with ChildStack to support predictive back animation
 */
interface PredictiveBackComponent : BackHandlerOwner {
    fun onBack()
}

/**
 * A convenient wrapper around [com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation]
 * for components implementing [PredictiveBackComponent].
 */
expect fun <C : Any, T : Any> PredictiveBackComponent.predictiveBackAnimation(): StackAnimation<C, T>
