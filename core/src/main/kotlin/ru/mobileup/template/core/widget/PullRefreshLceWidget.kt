package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
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
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.StringDesc
import ru.mobileup.template.core.theme.custom.CustomTheme
import ru.mobileup.template.core.utils.AbstractLoadableState

/**
 * Displays Replica state ([AbstractLoadableState]) with pull-to-refresh functionality.
 *
 * Note: a value of refreshing in [content] is true only when data is refreshing and pull gesture didn't occur.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> PullRefreshLceWidget(
    state: AbstractLoadableState<T>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars,
    loadingContent: @Composable BoxScope.(PaddingValues) -> Unit = {
        FullscreenCircularProgress(Modifier.windowInsetsPadding(contentWindowInsets))
    },
    errorContent: @Composable BoxScope.(StringDesc, PaddingValues) -> Unit = { error, _ ->
        ErrorPlaceholder(
            modifier = Modifier.windowInsetsPadding(contentWindowInsets),
            errorMessage = error.localized(),
            onRetryClick = onRetryClick
        )
    },
    content: @Composable BoxScope.(data: T, refreshing: Boolean, paddingValues: PaddingValues) -> Unit,
) {
    LceWidget(
        state = state,
        onRetryClick = onRetryClick,
        modifier = modifier,
        contentWindowInsets = contentWindowInsets,
        loadingContent = loadingContent,
        errorContent = errorContent,
    ) { data, refreshing, padding ->
        var pullGestureOccurred by remember { mutableStateOf(false) }

        LaunchedEffect(refreshing) {
            if (!refreshing) pullGestureOccurred = false
        }

        val pullRefreshState = rememberPullToRefreshState()
        val isRefreshing = pullGestureOccurred && refreshing

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                pullGestureOccurred = true
                onRefresh()
            },
            state = pullRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .windowInsetsPadding(contentWindowInsets.only(WindowInsetsSides.Top)),
                    state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    color = CustomTheme.colors.icon.primary,
                )
            },
        ) {
            content(data, refreshing && !pullGestureOccurred, padding)
        }
    }
}
