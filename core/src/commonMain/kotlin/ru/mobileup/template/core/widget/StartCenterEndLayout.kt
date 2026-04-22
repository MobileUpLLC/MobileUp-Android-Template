package ru.mobileup.template.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlin.math.max

@Composable
internal fun StartCenterEndLayout(
    modifier: Modifier = Modifier,
    start: (@Composable BoxScope.() -> Unit)? = null,
    center: (@Composable BoxScope.() -> Unit)? = null,
    end: (@Composable BoxScope.() -> Unit)? = null,
) {
    Layout(
        modifier = modifier,
        content = {
            Box { start?.invoke(this) }
            Box { center?.invoke(this) }
            Box { end?.invoke(this) }
        },
        measurePolicy = { measurables, constraints ->
            val sideConstraints = constraints.copy(
                minWidth = 0,
                maxWidth = constraints.maxWidth / 2
            )

            val startPlaceable = measurables[0].measure(sideConstraints)
            val endPlaceable = measurables[2].measure(sideConstraints)
            val centerMaxWidth = (constraints.maxWidth - startPlaceable.width - endPlaceable.width)
                .coerceAtLeast(0)

            val centerPlaceable = measurables[1].measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = centerMaxWidth
                )
            )

            val layoutWidth = constraints.maxWidth
            val layoutHeight = maxOf(
                startPlaceable.height,
                centerPlaceable.height,
                endPlaceable.height
            )
            val centeredX = layoutWidth / 2 - centerPlaceable.width / 2
            val minCenterX = startPlaceable.width
            val maxCenterX = layoutWidth - endPlaceable.width - centerPlaceable.width
            val centerX = when {
                maxCenterX < minCenterX -> minCenterX
                else -> centeredX.coerceIn(minCenterX, maxCenterX)
            }

            layout(width = layoutWidth, height = layoutHeight) {
                startPlaceable.placeRelative(
                    x = 0,
                    y = (layoutHeight - startPlaceable.height) / 2
                )
                centerPlaceable.placeRelative(
                    x = centerX,
                    y = (layoutHeight - centerPlaceable.height) / 2
                )
                endPlaceable.placeRelative(
                    x = max(0, layoutWidth - endPlaceable.width),
                    y = (layoutHeight - endPlaceable.height) / 2
                )
            }
        }
    )
}
