plugins {
    `kotlin-dsl`
}

group = "com.build.logic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)

    // Workaround for version catalog working inside precompiled scripts
    // Issue - https://github.com/gradle/gradle/issues/15383
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "custom.detekt"
            implementationClass = "DetektPlugin"
        }
        register("lint") {
            id = "custom.lint"
            implementationClass = "LintPlugin"
        }
    }
}
