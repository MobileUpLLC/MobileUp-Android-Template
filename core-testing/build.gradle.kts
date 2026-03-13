plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ru.mobileup.template.core_testing"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    // Expose types from core in public APIs (ComponentFactory, MessageService, etc.)
    api(project(":core"))

    // Public API types used by core-testing helpers
    api(libs.coroutines.core)
    api(libs.coroutines.test)
    api(libs.koin)
    api(libs.ktor.client.mock)
    api(libs.bundles.decompose)
    api(libs.bundles.replica)
    api(libs.kotest.runner.junit5)
}
