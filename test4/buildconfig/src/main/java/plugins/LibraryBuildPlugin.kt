package plugins


import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<TestedExtension> {
                plugins.run {
                    apply("libraries.detekt")
                    apply("libraries.githooks")
                    apply("libraries.spotless")
                }
            }
        }
    }
}