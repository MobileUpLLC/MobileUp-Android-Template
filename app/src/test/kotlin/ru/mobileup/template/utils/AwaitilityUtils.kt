package ru.mobileup.template.utils

import android.os.Looper
import org.awaitility.kotlin.await
import org.awaitility.kotlin.withPollInterval
import org.robolectric.Shadows
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Waits for a condition to be met.
 *
 * Note: it uses periodic polling to check a condition. Maybe it can be done in a better way.
 */
fun awaitUntil(
    timeout: Duration = 5.seconds,
    pollInterval: Duration = 5.milliseconds,
    condition: () -> Boolean
) {
    Shadows.shadowOf(Looper.getMainLooper()).idle()

    // Check the condition immediately
    if (condition()) return

    // Then check the condition periodically
    await
        .atMost(timeout.toJavaDuration())
        .withPollInterval(pollInterval.toJavaDuration())
        .pollInSameThread() // Use pollInSameThread() because idle() can't be called from a background thread
        .until {
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            condition()
        }
}