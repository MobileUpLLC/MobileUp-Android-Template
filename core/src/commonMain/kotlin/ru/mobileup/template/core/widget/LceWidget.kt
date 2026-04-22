package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mobileup.template.core.utils.AbstractLoadableState
import ru.mobileup.template.core.utils.StringDesc
import ru.mobileup.template.core.utils.withImePadding

/**
 * Displays an [AbstractLoadableState] using the common LCE pattern: loading, error, or loaded content.
 *
 * @param state - state to render.
 * If data is available, the content is shown; otherwise loading or error is displayed.
 * @param onRetryClick callback used by error UI.
 * @param innerPadding padding that the widget applies to its built-in UI states such as loading and error.
 * It is also passed to the main content as `contentPadding`.
 * @param showRefreshingProgress whether to show the top progress bar while already loaded data is refreshing.
 * @param applyImePadding whether to include IME bottom padding in the padding passed to loading, error, and content.
 * @param loadingContent loading UI that receives effective padding and is expected to respect it.
 * @param errorContent error UI that receives effective padding and is expected to respect it.
 * @param content main content of the widget. The provided `contentPadding` should be applied by
 * the caller to the main content so it respects the safe area and IME by default.
 */
@Composable
fun <T : Any> LceWidget(
    state: AbstractLoadableState<T>,
    onRetryClick: () -> Unit,
    innerPadding: PaddingValues,
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
    val loading = state.loading
    val data = state.data
    val error = state.error
    val contentPadding = if (applyImePadding) innerPadding.withImePadding() else innerPadding

    Box(modifier) {
        when {
            data != null -> {
                content(data, loading, contentPadding)

                if (showRefreshingProgress) {
                    RefreshingProgress(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(top = 4.dp),
                        active = loading
                    )
                }
            }

            loading -> loadingContent(contentPadding)

            error != null -> errorContent(error, contentPadding)
        }
    }
}
