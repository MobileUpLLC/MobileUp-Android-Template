plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
    id("io.gitlab.arturbosch.detekt")
}

android {
    val minSdkVersion: Int by rootProject.extra
    val targetSdkVersion: Int by rootProject.extra

    namespace = "ru.mobileup.template"
    compileSdk = targetSdkVersion

    defaultConfig {
        applicationId = "ru.mobileup.template"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        getByName("debug") {
        }

        create("release") {
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            signingConfig = signingConfigs["release"]
        }
    }

    setFlavorDimensions(listOf("backend"))
    productFlavors {
        create("dev") {
            dimension = "backend"
            buildConfigField("String", "BACKEND_URL", "\"https://pokeapi.co/\"")
        }

        create("prod") {
            dimension = "backend"
            buildConfigField("String", "BACKEND_URL", "\"https://pokeapi.co/\"")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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
    coreLibraryDesugaring(libs.android.desugar)

    // Modules
    implementation(project(":core"))
    implementation(project(":features"))

    // UI
    implementation(libs.activity.compose)
    implementation(libs.splashscreen)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    implementation(libs.replica.core)

    // DI
    implementation(libs.koin)

    // Debugging
    debugImplementation(libs.bundles.hyperion)
    debugImplementation(libs.chucker)
    debugImplementation(libs.replica.devtools)
    implementation(libs.logger.kermit)
}
