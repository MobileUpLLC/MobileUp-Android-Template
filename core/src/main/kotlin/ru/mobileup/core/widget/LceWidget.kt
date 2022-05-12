package ru.mobileup.core.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.aartikov.replica.single.Loadable
import ru.mobileup.core.error_handling.errorMessage
import ru.mobileup.core.utils.resolve

@Composable
fun <T : Any> LceWidget(
    state: Loadable<T>,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable (() -> Unit)? = null,
    content: @Composable (data: T, refreshing: Boolean) -> Unit
) {
    val (loading, data, error) = state
    when {
        error != null -> ErrorPlaceholder(
            errorMessage = error.exception.errorMessage.resolve(),
            onRetryClick = onRetryClick,
            modifier = modifier
        )

        loading -> FullscreenCircularProgress(modifier)

        data == null -> emptyContent?.invoke()

        data != null -> content(data, loading)
    }
}