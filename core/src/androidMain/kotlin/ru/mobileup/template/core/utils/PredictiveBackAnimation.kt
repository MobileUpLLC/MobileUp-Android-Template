package ru.mobileup.template.core.utils

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation

@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> PredictiveBackComponent.predictiveBackAnimation(): StackAnimation<C, T> = predictiveBackAnimation(
    backHandler = backHandler,
    fallbackAnimation = stackAnimation(fade() + slide()),
    selector = { backEvent, _, _ -> androidPredictiveBackAnimatableV2(backEvent) },
    onBack = ::onBack
)
