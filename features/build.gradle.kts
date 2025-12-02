plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.module.graph)
}

android {
    namespace = "ru.mobileup.template.features"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    packaging {
        resources.excludes += "META-INF/*"
    }
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
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.components.uiToolingPreview)
    implementation(libs.material.icons)
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
}

composeCompiler {
    stabilityConfigurationFiles.add(
        rootProject.layout.projectDirectory.file("stability_config.conf")
    )
}

// Usage: ./gradlew generateModuleGraph detectGraphCycles
moduleGraph {
    featuresPackage.set("ru.mobileup.template.features")
    featuresDirectory.set(project.file("src/main/kotlin/ru/mobileup/template/features"))
    outputDirectory.set(project.file("module_graph"))
}
