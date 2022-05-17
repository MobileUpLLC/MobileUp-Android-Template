package ru.mobileup.core.error_handling

abstract class ApplicationException(cause: Throwable? = null) : Exception(cause)

/**
 * No access to data (access token invalid or expired)
 */
class UnauthorizedException(cause: Throwable) : ApplicationException(cause)

/**
 * Received a response from the server, but it is invalid - 4xx, 5xx
 */
class ServerException(cause: Throwable) : ApplicationException(cause)

/**
 * Data transfer error
 */
abstract class TransportException(cause: Throwable) : ApplicationException(cause)

/**
 * Failed to connect to the server and the problem is most likely on the client
 */
class NoInternetException(cause: Throwable) : TransportException(cause)

/**
 * Failed to connect to the server and the problem is most likely on the server
 */
class NoServerResponseException(cause: Throwable) : TransportException(cause)

/**
 *  Problems parsing json or lack of fields
 */
class DeserializationException(cause: Throwable) : TransportException(cause)

/**
 * Indicated that the client and server cannot agree on the desired level of security.
 * The problem may be on the server - the certificate has expired.
 * The problem may be on the client - verification of the date and time of collection is required.
 */
class SSLHandshakeException(cause: Throwable) : TransportException(cause)

/**
 * Could not find app for action
 */
class ExternalAppNotFoundException(cause: Throwable) : ApplicationException(cause)

/**
 * The problem exists while working with the file
 */
class FileOperationException(cause: Throwable) : ApplicationException(cause)

/**
 * Some unknown issue
 */
class UnknownException(cause: Throwable, override val message: String) : ApplicationException(cause)