package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.StringDesc
import ru.mobileup.template.core.utils.AbstractLoadableState

/**
 * Displays Replica state ([AbstractLoadableState]).
 */
@Composable
fun <T : Any> LceWidget(
    state: AbstractLoadableState<T>,
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
    val loading = state.loading
    val data = state.data
    val error = state.error

    val padding = contentWindowInsets.asPaddingValues()

    Box(modifier) {
        when {
            data != null -> content(data, loading, padding)

            loading -> loadingContent(padding)

            error != null -> errorContent(error, padding)
        }
    }
}
