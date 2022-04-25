package ru.mobileup.core.error_handling

import me.aartikov.sesame.localizedstring.LocalizedString
import ru.mobileup.core.BuildConfig
import ru.mobileup.core.R

val Throwable.errorMessage: LocalizedString
    get() = when (this) {
        is ServerException, is DeserializationException -> LocalizedString.resource(R.string.error_invalid_response)

        is NoServerResponseException -> LocalizedString.resource(R.string.error_no_server_response)

        is NoInternetException -> LocalizedString.resource(R.string.error_no_internet_connection)

        is SSLHandshakeException -> LocalizedString.resource(R.string.error_ssl_handshake)

        is ExternalAppNotFoundException -> LocalizedString.resource(R.string.error_matching_application_not_found)

        else -> {
            val description = this.message
            if (description != null && BuildConfig.DEBUG) {
                LocalizedString.resource(R.string.error_unexpected_with_description, description)
            } else {
                LocalizedString.resource(R.string.error_unexpected)
            }
        }
    }