package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.mobileup.template.core.theme.custom.CustomTheme
import ru.mobileup.template.core.utils.AbstractLoadableState
import ru.mobileup.template.core.utils.StringDesc

/**
 * A pull-to-refresh version of [LceWidget]. It renders loading, error, and content states,
 * and also wraps the loaded content with pull-to-refresh behavior.
 *
 * @param state - state to render.
 * If data is available, the content is shown inside the pull-to-refresh container.
 * @param innerPadding padding that the widget applies to its built-in UI states,
 * including loading, error, and the pull-to-refresh indicator.
 * It is also passed to the main content as `contentPadding`.
 * @param onRefresh callback triggered by the pull-to-refresh gesture.
 * @param onRetryClick callback used by error UI, it equals to [onRefresh] by default.
 * @param showRefreshingProgress whether to show the top progress bar while already loaded data is refreshing.
 * @param applyImePadding whether to include IME bottom padding in the padding passed to loading, error, and content.
 * @param loadingContent loading UI that receives effective padding and is expected to respect it.
 * @param errorContent error UI that receives effective padding and is expected to respect it.
 * @param content main content of the widget. The provided `contentPadding` should be applied
 * by the caller to the main content so it respects the safe area and IME by default.
 *
 * Note: a value of refreshing in [content] is true only when data is refreshing and the pull gesture did not occur.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> PullRefreshLceWidget(
    state: AbstractLoadableState<T>,
    innerPadding: PaddingValues,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit = onRefresh,
    modifier: Modifier = Modifier,
    showRefreshingProgress: Boolean = true,
    loadingContent: @Composable BoxScope.(PaddingValues) -> Unit = { paddings ->
        FullscreenCircularProgress(
            modifier = Modifier.padding(paddings)
        )
    },
    errorContent: @Composable BoxScope.(StringDesc, PaddingValues) -> Unit = { error, paddings ->
        ErrorPlaceholder(
            modifier = Modifier.padding(paddings),
            errorMessage = error.resolve(),
            onRetryClick = onRetryClick
        )
    },
    applyImePadding: Boolean = true,
    content: @Composable BoxScope.(data: T, refreshing: Boolean, contentPadding: PaddingValues) -> Unit,
) {
    var pullGestureOccurred by remember { mutableStateOf(false) }
    val isPullRefreshIndicatorRefreshing = pullGestureOccurred && state.loading && state.data != null

    LaunchedEffect(state.loading) {
        if (!state.loading) pullGestureOccurred = false
    }

    LceWidget(
        state = state,
        innerPadding = innerPadding,
        onRetryClick = onRetryClick,
        modifier = modifier,
        showRefreshingProgress = showRefreshingProgress && !isPullRefreshIndicatorRefreshing,
        applyImePadding = applyImePadding,
        loadingContent = loadingContent,
        errorContent = errorContent,
    ) { data, refreshing, contentPadding ->
        val pullRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = isPullRefreshIndicatorRefreshing,
            onRefresh = {
                pullGestureOccurred = true
                onRefresh()
            },
            state = pullRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(Alignment.TopCenter),
                    state = pullRefreshState,
                    isRefreshing = isPullRefreshIndicatorRefreshing,
                    color = CustomTheme.colors.icon.primary,
                )
            },
        ) {
            content(data, refreshing && !pullGestureOccurred, contentPadding)
        }
    }
}
