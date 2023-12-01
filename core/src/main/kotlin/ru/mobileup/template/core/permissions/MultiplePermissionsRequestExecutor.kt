package ru.mobileup.template.core.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mobileup.template.core.activity.ActivityProvider

internal class MultiplePermissionsRequestExecutor(
    private val activityProvider: ActivityProvider
) {
    private var activityResultLauncher =
        MutableStateFlow<ActivityResultLauncher<Array<String>>?>(null)
    private val permissionsResultFlow = MutableSharedFlow<Map<String, Boolean>?>()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
            activityProvider.activityStateFlow.collect {
                if (it != null) {
                    registerLauncher(it)
                } else {
                    unregisterLauncher()
                }
            }
        }
    }

    suspend fun process(permissions: List<String>): MultiplePermissionResult {
        val permissionResults = mutableMapOf<String, SinglePermissionResult>()

        activityResultLauncher.filterNotNull().first().launch(permissions.toTypedArray())
        (permissionsResultFlow.first() ?: throw CancellationException()).forEach {
            val result = if (it.value) {
                SinglePermissionResult.Granted
            } else {
                val rationale =
                    activityProvider.awaitActivity().shouldShowRequestPermissionRationale(it.key)
                SinglePermissionResult.Denied(permanently = !rationale)
            }
            permissionResults[it.key] = result
        }
        return MultiplePermissionResult(
            value = permissionResults
        )
    }

    private fun registerLauncher(activity: ComponentActivity) {
        activityResultLauncher.value =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                activity.lifecycleScope.launch {
                    permissionsResultFlow.emit(it)
                }
            }
    }

    private suspend fun unregisterLauncher() {
        activityResultLauncher.value?.unregister()
        activityResultLauncher.value = null
        permissionsResultFlow.emit(null)
    }
}
