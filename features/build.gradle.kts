plugins {
    alias(libs.plugins.convetion.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.module.graph)
    alias(libs.plugins.kover)
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
    implementation(libs.compose.material.icons)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.coil)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    implementation(libs.form.validation)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.ktor.core)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kover {
    reports {
        filters {
            includes {
                // We focus coverage on tested behavior: Real components + repositories + data/domain models + DI wiring.
                classes(
                    "*.presentation.Real*Component",
                    "*.presentation.*.Real*Component",
                    "*.data.*RepositoryImpl",
                    "DIKt",
                    "*.DIKt"
                )
            }
        }
    }
}

// Usage: ./gradlew generateModuleGraph detectGraphCycles
moduleGraph {
    featuresPackage.set("ru.mobileup.template.features")
    featuresDirectory.set(project.file("src/main/kotlin/ru/mobileup/template/features"))
    outputDirectory.set(project.file("module_graph"))
}
