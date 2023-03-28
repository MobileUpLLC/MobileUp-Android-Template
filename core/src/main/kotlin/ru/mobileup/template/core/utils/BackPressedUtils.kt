package ru.mobileup.template.core.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Should be used to handle clicks on toolbar back button.
 *
 * Usage:
 * val context = LocalContext.current
 * ...
 * onClick = {
 *     dispatchOnBackPressed(context)
 * }
 */
fun dispatchOnBackPressed(context: Context) {
    val activity = context.getActivity() ?: return
    activity.onBackPressedDispatcher.onBackPressed()
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}