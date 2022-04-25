package ru.mobileup.template.utils

import android.os.Looper
import org.awaitility.kotlin.await
import org.awaitility.kotlin.withPollInterval
import org.robolectric.Shadows
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * A function that allows you to wait for a condition to be met.
 * You should be careful because function used "pollInSameThread" with conditions that wait forever
 * (or a long time) since Awaitility cannot interrupt the thread when it's using the same thread as the test.
 */
fun awaitUntil(condition: () -> Boolean) {
    Shadows.shadowOf(Looper.getMainLooper()).idle()

    // Check the condition immediately
    if (condition()) return

    // Then check the condition periodically
    await
        .atMost(Duration.of(5, ChronoUnit.SECONDS))
        .withPollInterval(Duration.of(10, ChronoUnit.MILLIS))
        .pollInSameThread() // Use pollInSameThread() because idle() can't be called from a background thread
        .until {
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            condition()
        }
}