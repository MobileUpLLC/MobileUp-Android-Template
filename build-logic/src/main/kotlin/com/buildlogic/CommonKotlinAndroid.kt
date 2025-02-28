package com.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = with(commonExtension) {

    val minSdkVersion = libs.versions.minSdk.get().toInt()
    val targetSdkVersion = libs.versions.targetSdk.get().toInt()
    val compileSdkVersion = libs.versions.compileSdk.get().toInt()

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        lint.targetSdk = targetSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
            )
        }
    }
}
