package ru.mobileup.template.core.error_handling

import dev.icerock.moko.resources.desc.StringDesc
import ru.mobileup.template.core.BuildConfig
import ru.mobileup.template.core.R
import ru.mobileup.template.core.utils.Resource
import ru.mobileup.template.core.utils.ResourceFormatted

/**
 * Returns human readable messages for exceptions.
 */
val Exception.errorMessage: StringDesc
    get() = when (this) {

        is ServerException, is DeserializationException -> StringDesc.Resource(R.string.error_invalid_response)

        is NoServerResponseException -> StringDesc.Resource(R.string.error_no_server_response)

        is NoInternetException -> StringDesc.Resource(R.string.error_no_internet_connection)

        is SSLHandshakeException -> StringDesc.Resource(R.string.error_ssl_handshake)

        is ExternalAppNotFoundException -> StringDesc.Resource(R.string.error_matching_application_not_found)

        else -> {
            val description = this.message
            if (description != null && BuildConfig.DEBUG) {
                StringDesc.ResourceFormatted(
                    R.string.error_unexpected_with_description,
                    description
                )
            } else {
                StringDesc.Resource(R.string.error_unexpected)
            }
        }
    }
