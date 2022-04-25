package ru.mobileup.core.error_handling

abstract class ApplicationException(cause: Throwable? = null) : Exception(cause)

/**
 * Отсутствие доступа к данным (Access token невалиден или истек)
 */
class UnauthorizedException(cause: Throwable) : ApplicationException(cause)

/**
 * Получен ответ от сервера, но он невалиден - 4xx, 5xx
 */
class ServerException(cause: Throwable? = null) : ApplicationException(cause)

/**
 * Ошибка передачи данных
 */
abstract class TransportException(cause: Throwable? = null) : ApplicationException(cause)

/**
 * Не удалось подключиться к серверу и проблема скорее всего у клиента
 */
class NoInternetException : TransportException()

/**
 *  Не удалось подключиться к серверу и проблема скорее всего на сервере
 */
class NoServerResponseException : TransportException()

/**
 * Проблемы при парсинге json или нехватка полей
 */
class DeserializationException(cause: Throwable) : TransportException(cause)

/**
 * Указывает, что клиент и сервер не смогли согласовать желаемый уровень безопасности.
 * Проблема может быть на сервере - истек сертификат.
 * Проблема может быть у клиента - требуется проверка даты и времени на устройстве.
 */
class SSLHandshakeException : TransportException()

/**
 * Не удалось найти приложение для работы с переденными данными
 */
class ExternalAppNotFoundException(cause: Throwable) : ApplicationException(cause)

/**
 * Проблема возникла при работе с файлом
 */
class FileOperationException(cause: Throwable) : ApplicationException(cause)

/**
 * Какая-то неизвестная проблема
 */
class UnknownException(cause: Throwable, override val message: String) : ApplicationException(cause)