package ru.mobileup.template.core.permissions

/**
 * A result of multiple permissions request.
 */
class MultiplePermissionResult(
    val value: Map<String, SinglePermissionResult>
) {
    companion object {
        fun buildGranted(permissions: List<String>) = MultiplePermissionResult(
            value = permissions.associateWith {
                SinglePermissionResult.Granted
            }
        )
    }

    val isEmpty: Boolean
        get() = value.isEmpty()

    val isAllGranted: Boolean
        get() = value.isNotEmpty() && value.all { it.value is SinglePermissionResult.Granted }

    val isAllDenied: Boolean
        get() = value.isNotEmpty() && value.all { it.value is SinglePermissionResult.Denied }

    operator fun plus(second: MultiplePermissionResult) = MultiplePermissionResult(
        value = this.value + second.value
    )
}
