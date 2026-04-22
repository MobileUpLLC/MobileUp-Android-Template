package ru.mobileup.template.core.configuration

enum class PlatformType {
    Android,
    Ios,
    Jvm // for testing on Desktop
}

expect class Platform {
    val type: PlatformType
}
