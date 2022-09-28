package ru.mobileup.template.core.permissions

/**
 * A result of single permission request.
 */
sealed class SinglePermissionResult {

    /**
     * Permission has been granted by user
     */
    object Granted : SinglePermissionResult()

    /**
     * Permission has been denied by user
     * If [permanently] == true permission was denied permanently (user chose "Never ask again")
     */
    class Denied(val permanently: Boolean) : SinglePermissionResult()
}
