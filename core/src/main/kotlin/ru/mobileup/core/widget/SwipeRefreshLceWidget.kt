package ru.mobileup.core.widget

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.aartikov.replica.single.Loadable

@Composable
fun <T : Any> SwipeRefreshLceWidget(
    state: Loadable<T>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (data: T, refreshing: Boolean) -> Unit
) {
    LceWidget(
        state = state,
        onRetryClick = onRetryClick,
        modifier = modifier,
    ) { data, refreshing ->
        StatefulSwipeRefresh(
            refreshing = refreshing,
            onRefresh = onRefresh,
            indicator = { s, trigger ->
                SwipeRefreshIndicator(
                    s, trigger,
                    contentColor = MaterialTheme.colors.primaryVariant
                )
            }
        ) { pullToRefreshPulled ->
            content(data, refreshing = refreshing && !pullToRefreshPulled)
        }
    }
}

@Composable
fun StatefulSwipeRefresh(
    refreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    indicator: @Composable (state: SwipeRefreshState, refreshTrigger: Dp) -> Unit = { s, trigger ->
        SwipeRefreshIndicator(s, trigger)
    },
    content: @Composable (pullToRefreshPulled: Boolean) -> Unit
) {
    var pullToRefreshPulled by remember { mutableStateOf(false) }

    LaunchedEffect(refreshing) {
        if (!refreshing) pullToRefreshPulled = false
    }

    val state = rememberSwipeRefreshState(pullToRefreshPulled && refreshing)

    SwipeRefresh(
        state = state,
        onRefresh = {
            pullToRefreshPulled = true
            onRefresh()
        },
        indicator = indicator,
        modifier = modifier
    ) {
        content(pullToRefreshPulled)
    }
}