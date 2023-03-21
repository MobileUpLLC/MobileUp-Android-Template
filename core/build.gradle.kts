plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
    id("io.gitlab.arturbosch.detekt")
    id("com.google.devtools.ksp")
    id("de.jensklingenberg.ktorfit")
}

android {
    val minSdkVersion: Int by rootProject.extra
    val targetSdkVersion: Int by rootProject.extra

    compileSdk = targetSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = minSdkVersion
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packagingOptions {
        resources.excludes += "META-INF/*"
    }
}

dependencies {
    ksp(libs.ktorfit.ksp)

    // Kotlin
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Serialization
    implementation(libs.serialization.json)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)

    // Debugging
    debugImplementation(libs.chucker)
    debugImplementation(libs.bundles.hyperion)
    debugImplementation(libs.replica.devtools)
}
