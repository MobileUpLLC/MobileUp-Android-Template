plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
    id("io.gitlab.arturbosch.detekt")
    id("ru.mobileup.module-graph")
    id("com.google.devtools.ksp")
    id("de.jensklingenberg.ktorfit")
}

android {
    val minSdkVersion: Int by rootProject.extra
    val targetSdkVersion: Int by rootProject.extra

    compileSdk = targetSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
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

    // Modules
    implementation(project(":core"))

    // Kotlin
    implementation(libs.kotlin.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.activity.compose)
    implementation(libs.compose.tooling)
    implementation(libs.bundles.accompanist)
    implementation(libs.coil)

    // DI
    implementation(libs.koin)

    // Serialization
    implementation(libs.serialization.json)

    // Network
    implementation(libs.ktorfit.lib)
    implementation(libs.ktor.android)

    // Architecture
    implementation(libs.sesame.localizedString)
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)

    // Debugging
    implementation(libs.logger.kermit)
}

// Usage: ./gradlew generateModuleGraph detectGraphCycles
moduleGraph {
    featuresPackage.set("ru.mobileup.template")
    featuresDirectory.set(project.file("src/main/kotlin/ru/mobileup/template/features"))
    outputDirectory.set(project.file("module_graph"))
}
