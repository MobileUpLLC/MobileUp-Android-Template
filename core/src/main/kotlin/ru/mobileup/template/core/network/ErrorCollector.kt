package ru.mobileup.template.core.network

import ru.mobileup.template.core.error_handling.ApplicationException

fun interface ErrorCollector {
    fun collectNetworkError(exception: ApplicationException)
}
