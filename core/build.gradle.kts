plugins {
    alias(libs.plugins.convetion.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "ru.mobileup.template.core"
}

dependencies {
    ksp(libs.ktorfit.ksp)

    // Kotlin
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.bundles.compose)
    implementation(libs.compose.material.icons)
    // TODO(Alpha version is required due to the usage of SecureTextField, remove when a stable release is available)
    implementation("androidx.compose.material3:material3:1.4.0-alpha12")
    implementation(libs.bundles.accompanist)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    implementation(libs.security.crypto)
    implementation(libs.security.crypto.ktx)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)

    implementation(libs.form.validation)

    // Debugging
    debugImplementation(libs.chucker)
    debugImplementation(libs.bundles.hyperion)
    debugImplementation(libs.replica.devtools)
}
