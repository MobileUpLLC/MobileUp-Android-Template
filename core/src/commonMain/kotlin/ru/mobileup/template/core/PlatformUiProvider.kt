package ru.mobileup.template.core

import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandler

/**
 * Should be implemented by platforms and provided through LocalSystemBarIconsColorHandler
 */
interface PlatformUiProvider {
    fun <C : Any, T : Any> createPredictiveBackAnimation(
        backHandler: BackHandler,
        onBack: () -> Unit
    ): StackAnimation<C, T>
}

val LocalPlatformUiProvider = staticCompositionLocalOf<PlatformUiProvider?> { null }