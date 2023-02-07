val minSdkVersion by extra(23)
val targetSdkVersion by extra(33)

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    afterEvaluate {
        apply {
            from("$rootDir/code_quality/lint/lint.gradle")
            from("$rootDir/code_quality/detekt/detekt.gradle")
        }
    }
}
