pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val kotlinVersion = "1.9.22"
    val androidPluginVersion = "8.1.1"
    val kspVersion = "1.9.22-1.0.17"
    val ktorfitVersion = "1.12.0"
    val mokoResourcesVersion = "0.23.0"
    val moduleGraphVersion = "1.3.3"
    val detektVersion = "1.23.1"

    plugins {
        id("com.android.application") version androidPluginVersion
        id("com.android.library") version androidPluginVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("ru.mobileup.module-graph") version moduleGraphVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.parcelize") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
        id("de.jensklingenberg.ktorfit") version ktorfitVersion
        id("dev.icerock.mobile.multiplatform-resources") version mokoResourcesVersion
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
            val dateTimeVersion = "0.4.1"
            library("kotlinx-datetime", "org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")

            // Concurrency
            val coroutinesVersion = "1.7.3"
            library("coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            library("coroutines-android", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

            // Architecture
            val decomposeVersion = "2.2.2"
            library("decompose-core", "com.arkivanov.decompose:decompose:$decomposeVersion")
            library("decompose-compose", "com.arkivanov.decompose:extensions-compose-jetpack:$decomposeVersion")
            bundle("decompose", listOf("decompose-core", "decompose-compose"))

            // Network
            val ktorVersion = "2.3.6"
            val ktorfitVersion = "1.12.0"
            library("ktor-core", "io.ktor:ktor-client-core:$ktorVersion")
            library("ktor-auth", "io.ktor:ktor-client-auth:$ktorVersion")
            library("ktor-serialization", "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            library("ktor-content-negotiation", "io.ktor:ktor-client-content-negotiation:$ktorVersion")
            library("ktor-logging", "io.ktor:ktor-client-logging:$ktorVersion")
            library("ktor-android", "io.ktor:ktor-client-okhttp:$ktorVersion")
            library("ktorfit-lib", "de.jensklingenberg.ktorfit:ktorfit-lib:$ktorfitVersion")
            library("ktorfit-ksp", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
            bundle(
                "ktor",
                listOf(
                    "ktor-core",
                    "ktor-auth",
                    "ktor-serialization",
                    "ktor-content-negotiation",
                    "ktor-logging",
                    "ktor-android"
                )
            )

            // Replica
            val replicaVersion = "1.3.1-alpha1"
            library("replica-core", "com.github.aartikov:replica-core:$replicaVersion")
            library("replica-algebra", "com.github.aartikov:replica-algebra:$replicaVersion")
            library("replica-android-network", "com.github.aartikov:replica-android-network:$replicaVersion")
            library("replica-decompose", "com.github.aartikov:replica-decompose:$replicaVersion")
            library("replica-devtools", "com.github.aartikov:replica-devtools:$replicaVersion")
            bundle(
                "replica",
                listOf(
                    "replica-core",
                    "replica-algebra",
                    "replica-android-network",
                    "replica-decompose"
                )
            )

            // DI
            val koinVersion = "3.5.0"
            library("koin", "io.insert-koin:koin-core:$koinVersion")

            // Logging
            val kermitVersion = "1.2.2"
            library("logger-kermit", "co.touchlab:kermit:$kermitVersion")

            // Code quality
            val detektVersion = "1.23.1"
            library("detekt-formatting", "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

            // Android
            val androidDesugarVersion = "2.0.2"
            library("android-desugar", "com.android.tools:desugar_jdk_libs:$androidDesugarVersion")

            // Android UI
            val composeVersion = "1.6.1"
            version("composeCompiler", "1.5.9")
            val activityVersion = "1.7.2"
            val coilVersion = "2.4.0"
            val splashscreenVersion = "1.0.0"
            val accompanistVersion = "0.32.0"
            val material3Version = "1.2.0"
            library("compose-ui", "androidx.compose.ui:ui:$composeVersion")
            library("compose-material", "androidx.compose.material3:material3:$material3Version")
            library("compose-tooling", "androidx.compose.ui:ui-tooling:$composeVersion")
            library("activity-compose", "androidx.activity:activity-compose:$activityVersion")
            library("activity", "androidx.activity:activity-ktx:$activityVersion")
            bundle(
                "compose",
                listOf(
                    "compose-ui",
                    "compose-material",
                    "compose-tooling",
                    "activity-compose"
                )
            )

            library("coil", "io.coil-kt:coil-compose:$coilVersion")
            library("splashscreen", "androidx.core:core-splashscreen:$splashscreenVersion")

            library("accompanist-systemuicontroller", "com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
            library("accompanist-swiperefresh", "com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
            bundle(
                "accompanist",
                listOf(
                    "accompanist-systemuicontroller",
                    "accompanist-swiperefresh"
                )
            )

            // Resources
            val mokoResourcesVersion = "0.23.0"
            library("moko-resources", "dev.icerock.moko:resources:$mokoResourcesVersion")
            library("moko-resourcesCompose", "dev.icerock.moko:resources-compose:$mokoResourcesVersion")

            // Debug tools
            val chuckerVersion = "4.0.0"
            val hyperionVersion = "0.9.38"
            val hyperionAddonsVersion = "0.3.3"
            library("chucker", "com.github.chuckerteam.chucker:library:$chuckerVersion")
            library("hyperion-core", "com.willowtreeapps.hyperion:hyperion-core:$hyperionVersion")
            library("hyperion-recorder", "com.willowtreeapps.hyperion:hyperion-recorder:$hyperionVersion")
            library("hyperion-crash", "com.willowtreeapps.hyperion:hyperion-crash:$hyperionVersion")
            library("hyperion-disk", "com.willowtreeapps.hyperion:hyperion-disk:$hyperionVersion")
            library("hyperion-addons-logs", "me.nemiron.hyperion:logs:$hyperionAddonsVersion")
            library("hyperion-addons-networkEmulation", "me.nemiron.hyperion:network-emulation:$hyperionAddonsVersion")
            library("hyperion-addons-chucker", "me.nemiron.hyperion:chucker:$hyperionAddonsVersion")
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
        }
    }
}
