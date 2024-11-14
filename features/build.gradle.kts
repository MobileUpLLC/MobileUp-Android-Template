plugins {
    alias(libs.plugins.convetion.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.module.graph)
}

android {
    namespace = "ru.mobileup.template.features"
}

dependencies {
    ksp(libs.ktorfit.ksp)

    // Modules
    implementation(project(":core"))

    // Kotlin
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.coil)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)
}

// Usage: ./gradlew generateModuleGraph detectGraphCycles
moduleGraph {
    featuresPackage.set("ru.mobileup.template.features")
    featuresDirectory.set(project.file("src/main/kotlin/ru/mobileup/template/features"))
    outputDirectory.set(project.file("module_graph"))
}
