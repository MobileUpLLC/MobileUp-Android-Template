package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.mobileup.template.core.utils.LoadableState

/**
 * Displays Replica state ([LoadableState]) with pull-to-refresh functionality.
 *
 * Note: a value of refreshing in [content] is true only when data is refreshing and pull gesture didn't occur.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> PullRefreshLceWidget(
    state: LoadableState<T>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    pullRefreshIndicator: @Composable (state: PullRefreshState, refreshing: Boolean) -> Unit = { s, refreshing ->
        PullRefreshIndicator(refreshing = refreshing, state = s, contentColor = MaterialTheme.colors.primaryVariant)
    },
    content: @Composable (data: T, refreshing: Boolean) -> Unit
) {
    LceWidget(
        state = state,
        onRetryClick = onRetryClick,
        modifier = modifier
    ) { data, refreshing ->
        var pullGestureOccurred by remember { mutableStateOf(false) }

        LaunchedEffect(refreshing) {
            if (!refreshing) pullGestureOccurred = false
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = pullGestureOccurred && refreshing,
            onRefresh = onRefresh
        )

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
        ) {
            pullRefreshIndicator(
                pullRefreshState,
                pullGestureOccurred && refreshing
            )
            content(
                data,
                refreshing && !pullGestureOccurred
            )
        }
    }
}