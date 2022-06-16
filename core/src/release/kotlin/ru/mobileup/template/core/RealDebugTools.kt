package ru.mobileup.template.core

import android.content.Context
import me.aartikov.replica.client.ReplicaClient
import okhttp3.Interceptor
import ru.mobileup.template.core.debug_tools.DebugTools

@Suppress("UNUSED_PARAMETER")
class RealDebugTools(
    context: Context,
    replicaClient: ReplicaClient
) : DebugTools {

    override val interceptors: List<Interceptor> = emptyList()

    override fun launch() {
        // do nothing
    }

    override fun collectError(exception: Exception) {
        // do nothing
    }
}