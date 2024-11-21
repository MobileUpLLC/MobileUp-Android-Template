import com.buildlogic.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

private fun DependencyHandlerScope.detektPlugins(dependencyNotation: Any) {
    add("detektPlugins", dependencyNotation)
}

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.detekt.get().pluginId)

        extensions.configure<DetektExtension> {
            toolVersion = libs.versions.detekt.get()
            source.setFrom(files(rootDir))
            config.setFrom(files("$rootDir/code_quality/detekt/config.yml"))
            parallel = true
            ignoreFailures = false
            disableDefaultRuleSets = true
        }

        dependencies {
            detektPlugins(libs.detekt.formatting)
            detektPlugins(libs.detekt.compose.rules)
        }

        tasks.withType<Detekt>().configureEach {
            exclude(
                "**/build/**",
                "**/resources/**",
                "code_quality/**",
                "geminio/**",
                "git_hooks/**"
            )
            reports {
                xml {
                    outputLocation.set(file("build/reports/detekt-results.xml"))
                    required.set(true)
                }
                html {
                    outputLocation.set(file("build/reports/detekt-results.html"))
                    required.set(true)
                }
                txt.required.set(false)
                md.required.set(false)
                sarif.required.set(false)
            }
        }
    }
}
