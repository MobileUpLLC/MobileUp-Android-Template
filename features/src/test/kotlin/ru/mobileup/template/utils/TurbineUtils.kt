package ru.mobileup.template.utils

import app.cash.turbine.ReceiveTurbine
import ru.mobileup.template.core.utils.LoadableState
import ru.mobileup.template.core.utils.PagedState

@JvmName("awaitMatchingLoadable")
suspend fun <T : Any> ReceiveTurbine<LoadableState<T>>.awaitMatching(
    predicate: (LoadableState<T>) -> Boolean
): LoadableState<T> {
    var state = awaitItem()
    while (!predicate(state)) {
        state = awaitItem()
    }
    return state
}

@JvmName("awaitMatchingPaged")
suspend fun <T : Any> ReceiveTurbine<PagedState<T>>.awaitMatching(
    predicate: (PagedState<T>) -> Boolean
): PagedState<T> {
    var state = awaitItem()
    while (!predicate(state)) {
        state = awaitItem()
    }
    return state
}
