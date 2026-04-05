package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mobileup.template.core.utils.AbstractLoadableState
import ru.mobileup.template.core.utils.StringDesc

/**
 * Displays an [AbstractLoadableState] using the common LCE pattern: loading, error, or loaded content.
 *
 * @param state - state to render.
 * If data is available, the content is shown; otherwise loading or error is displayed.
 * @param onRetryClick callback used by error UI.
 * @param innerPadding padding that the widget applies to its built-in UI states such as loading and error.
 * This does not automatically pad the main content container.
 * @param loadingContent loading UI that receives [innerPadding] and is expected to respect it.
 * @param errorContent error UI that receives [innerPadding] and is expected to respect it.
 * @param content main content of the widget. The provided `contentPadding` should be applied by
 * the caller to the main content so it respects the safe area.
 */
@Composable
fun <T : Any> LceWidget(
    state: AbstractLoadableState<T>,
    onRetryClick: () -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
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
    content: @Composable BoxScope.(data: T, refreshing: Boolean, contentPadding: PaddingValues) -> Unit,
) {
    val loading = state.loading
    val data = state.data
    val error = state.error

    Box(modifier) {
        when {
            data != null -> content(data, loading, innerPadding)

            loading -> loadingContent(innerPadding)

            error != null -> errorContent(error, innerPadding)
        }
    }
}
