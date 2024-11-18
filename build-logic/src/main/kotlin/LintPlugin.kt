import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LintPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        extensions.configure<BaseAppModuleExtension> {
            lint {
                abortOnError = true
                warningsAsErrors = false
                checkDependencies = true
                lintConfig = rootProject.file("code_quality/lint/lint-config.xml")
            }
        }
    }
}
