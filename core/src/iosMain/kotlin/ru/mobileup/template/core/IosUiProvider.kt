package ru.mobileup.template.core

import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.essenty.backhandler.BackHandler
import ru.mobileup.template.core.utils.predictiveBackAnimation

class IosUiProvider : PlatformUiProvider {
    override fun <C : Any, T : Any> createPredictiveBackAnimation(
        backHandler: BackHandler,
        onBack: () -> Unit
    ): StackAnimation<C, T> = predictiveBackAnimation(backHandler, onBack)
}
