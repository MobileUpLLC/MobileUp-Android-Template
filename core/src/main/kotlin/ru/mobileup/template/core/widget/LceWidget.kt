package ru.mobileup.template.core.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.template.core.utils.AbstractLoadableState

/**
 * Displays Replica state ([AbstractLoadableState]).
 */
@Composable
fun <T : Any> LceWidget(
    state: AbstractLoadableState<T>,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (data: T, refreshing: Boolean) -> Unit
) {
    val loading = state.loading
    val data = state.data
    val error = state.error

    when {
        data != null -> content(data, loading)

        loading -> FullscreenCircularProgress(modifier)

        error != null -> ErrorPlaceholder(
            errorMessage = error.localized(),
            onRetryClick = onRetryClick,
            modifier = modifier
        )
    }
}
