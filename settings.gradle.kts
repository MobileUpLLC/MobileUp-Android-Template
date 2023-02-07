pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val kotlinVersion = "1.8.0"
    val androidPluginVersion = "7.4.0"
    val detektVersion = "1.22.0"
    val moduleGraphVersion = "1.3.3"

    plugins {
        id("com.android.application") version androidPluginVersion
        id("com.android.library") version androidPluginVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("ru.mobileup.module-graph") version moduleGraphVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.parcelize") version kotlinVersion
    }
}

rootProject.name = "MobileUp Android Template"

include(":app")
include(":core")
include(":features")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {

            // Kotlin
            val dateTimeVersion = "0.4.0"
            library("kotlin-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").version(
                dateTimeVersion
            )

            val coroutinesVersion = "1.6.4"
            library("coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version(
                coroutinesVersion
            )

            library(
                "coroutines-android",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-android"
            ).version(coroutinesVersion)

            // Android
            library("android-desugar", "com.android.tools", "desugar_jdk_libs").version("1.1.5")
            library(
                "androidx-lifecycle-process",
                "androidx.lifecycle",
                "lifecycle-process"
            ).version("2.4.0")

            // UI
            val composeVersion = "1.4.0-alpha05"
            version("composeCompiler", "1.4.0")

            library("compose-ui", "androidx.compose.ui", "ui").version(composeVersion)
            library("compose-material", "androidx.compose.material", "material").version(
                composeVersion
            )
            library("compose-tooling", "androidx.compose.ui", "ui-tooling").version(
                composeVersion
            )
            library("activity-compose", "androidx.activity", "activity-compose").version("1.5.0")
            library("appcompat", "androidx.appcompat", "appcompat").version("1.5.0")
            library("coil", "io.coil-kt", "coil-compose").version("2.1.0")
            library("splashscreen", "androidx.core", "core-splashscreen").version("1.0.0")

            val accompanistVersion = "0.24.13-rc"

            library(
                "accompanist-systemuicontroller",
                "com.google.accompanist",
                "accompanist-systemuicontroller"
            ).version(accompanistVersion)
            library(
                "accompanist-swiperefresh",
                "com.google.accompanist",
                "accompanist-swiperefresh"
            ).version(accompanistVersion)
            bundle(
                "accompanist",
                listOf("accompanist-systemuicontroller", "accompanist-swiperefresh")
            )

            // Architecture
            val sesameVersion = "1.5.0"

            library(
                "sesame-localizedString",
                "com.github.aartikov",
                "sesame-localized-string"
            ).version(sesameVersion)
            library("sesame-dialog", "com.github.aartikov", "sesame-dialog").version(sesameVersion)
            library("sesame-composeForm", "com.github.aartikov", "sesame-compose-form").version(
                sesameVersion
            )
            bundle(
                "sesame",
                listOf("sesame-localizedString", "sesame-dialog", "sesame-composeForm")
            )

            val decomposeVersion = "0.8.0"

            library("decompose-core", "com.arkivanov.decompose", "decompose").version(
                decomposeVersion
            )
            library(
                "decompose-compose",
                "com.arkivanov.decompose",
                "extensions-compose-jetpack"
            ).version(decomposeVersion)
            bundle("decompose", listOf("decompose-core", "decompose-compose"))

            val replicaVersion = "1.0.0-alpha6"

            library("replica-core", "com.github.aartikov", "replica-core").version(replicaVersion)
            library("replica-algebra", "com.github.aartikov", "replica-algebra").version(
                replicaVersion
            )
            library(
                "replica-android-network",
                "com.github.aartikov",
                "replica-android-network"
            ).version(replicaVersion)
            library("replica-decompose", "com.github.aartikov", "replica-decompose").version(
                replicaVersion
            )
            library("replica-devtools", "com.github.aartikov", "replica-devtools").version(
                replicaVersion
            )
            bundle(
                "replica",
                listOf(
                    "replica-core",
                    "replica-algebra",
                    "replica-android-network",
                    "replica-decompose"
                )
            )

            // Serialization
            val serializationVersion = "1.3.3"
            version("serialization", "1.3.3")
            library(
                "serialization-core",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-core"
            ).version(serializationVersion)
            library(
                "serialization-json",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-json"
            ).version(serializationVersion)

            // Network
            library("retrofit-core", "com.squareup.retrofit2", "retrofit").version("2.9.0")
            library(
                "retrofit-converter-serialization",
                "com.jakewharton.retrofit",
                "retrofit2-kotlinx-serialization-converter"
            ).version("0.8.0")
            library(
                "okhttp-logging",
                "com.squareup.okhttp3",
                "logging-interceptor"
            ).version("4.9.2")

            // DI
            library("koin", "io.insert-koin", "koin-core").version("3.2.0")

            // Code quality
            library(
                "detekt-formatting",
                "io.gitlab.arturbosch.detekt",
                "detekt-formatting"
            ).version("1.21.0")

            // Debugging
            library("timber", "com.jakewharton.timber", "timber").version("5.0.1")

            val hyperionVersion = "0.9.34"
            val hyperionAddonsVersion = "0.3.3"

            library("hyperion-core", "com.willowtreeapps.hyperion", "hyperion-core").version(
                hyperionVersion
            )
            library(
                "hyperion-recorder",
                "com.willowtreeapps.hyperion",
                "hyperion-recorder"
            ).version(hyperionVersion)
            library("hyperion-crash", "com.willowtreeapps.hyperion", "hyperion-crash").version(
                hyperionVersion
            )
            library("hyperion-disk", "com.willowtreeapps.hyperion", "hyperion-disk").version(
                hyperionVersion
            )
            library("hyperion-addons-logs", "me.nemiron.hyperion", "logs").version(
                hyperionAddonsVersion
            )
            library(
                "hyperion-addons-networkEmulation",
                "me.nemiron.hyperion",
                "network-emulation"
            ).version(hyperionAddonsVersion)
            library("hyperion-addons-chucker", "me.nemiron.hyperion", "chucker").version(
                hyperionAddonsVersion
            )
            bundle(
                "hyperion",
                listOf(
                    "hyperion-core",
                    "hyperion-recorder",
                    "hyperion-crash",
                    "hyperion-disk",
                    "hyperion-addons-logs",
                    "hyperion-addons-networkEmulation",
                    "hyperion-addons-chucker"
                )
            )
            library("chucker", "com.github.chuckerteam.chucker", "library").version("3.5.2")

            // Testing
            library("junit", "junit", "junit").version("4.13.2")
            library("extJunit", "androidx.test.ext", "junit-ktx").version("1.1.3")
            library("kotlinTest", "org.jetbrains.kotlin", "kotlin-test").version("1.7.10")
            library("mockWebServer", "com.squareup.okhttp3", "mockwebserver").version("4.3.1")
            library("awaitility", "org.awaitility", "awaitility-kotlin").version("4.2.0")
            library("robolectric", "org.robolectric", "robolectric").version("4.8")
        }
    }
}
