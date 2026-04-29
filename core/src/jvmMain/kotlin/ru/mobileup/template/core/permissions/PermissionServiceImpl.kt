package ru.mobileup.template.core.permissions

class PermissionServiceImpl : PermissionService {

    override suspend fun requestPermission(permission: Permission): PermissionResult {
        return PermissionResult.Denied(permanently = true)
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        return false
    }
}
