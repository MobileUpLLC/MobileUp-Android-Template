package ru.mobileup.template.core.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.mobileup.template.core.activity.ActivityProvider

/**
 * Allows to request permissions by a suspend function call.
 *
 * When a permission request is called multiple times, a queue is formed and processed sequentially.
 *
 * Must be tied to [ActivityProvider].
 */
class PermissionService(
    private val activityProvider: ActivityProvider,
    private val applicationContext: Context
) {
    private val singlePermissionRequestExecutor = SinglePermissionRequestExecutor(activityProvider)
    private val multiplePermissionsRequestExecutor =
        MultiplePermissionsRequestExecutor(activityProvider)
    private val mutex = Mutex()

    /**
     * Request single permission. Should be called from a coroutine.
     */
    suspend fun requestPermission(permission: String): SinglePermissionResult {
        if (isPermissionGranted(permission)) {
            return SinglePermissionResult.Granted
        }

        return mutex.withLock {
            singlePermissionRequestExecutor.process(permission)
        }
    }

    /**
     * Request multiple permissions. Should be called from a coroutine.
     */
    suspend fun requestPermissions(permissions: List<String>): MultiplePermissionResult {
        val (grantedPermissions, notGrantedPermissions) = permissions.partition {
            isPermissionGranted(it)
        }
        val grantedPermissionsResult = MultiplePermissionResult.buildGranted(grantedPermissions)

        if (notGrantedPermissions.isEmpty()) {
            return grantedPermissionsResult
        }

        return mutex.withLock {
            grantedPermissionsResult + (multiplePermissionsRequestExecutor.process(
                notGrantedPermissions
            ))
        }
    }

    /**
     * Returns if a [permission] has been granted.
     */
    fun isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
        applicationContext,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Returns whether UI with rationale should be shown before requesting a permission.
     */
    suspend fun shouldShowRequestPermissionRationale(permission: String) =
        activityProvider.awaitActivity().shouldShowRequestPermissionRationale(permission)
}
