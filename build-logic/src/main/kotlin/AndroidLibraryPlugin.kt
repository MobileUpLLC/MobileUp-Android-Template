import com.android.build.gradle.LibraryExtension
import com.buildlogic.configureAndroidCompose
import com.buildlogic.configureKotlinAndroid
import com.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.plugins.android.library.get().pluginId)
            apply(libs.plugins.kotlin.android.get().pluginId)
            apply(libs.plugins.compose.compiler.get().pluginId)
        }

        extensions.configure<LibraryExtension> {
            configureAndroidCompose(this)
            configureKotlinAndroid(this)
            packaging.resources.excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/INDEX.LIST",
                "/META-INF/io.netty.versions.properties"
            )
        }
    }
}
