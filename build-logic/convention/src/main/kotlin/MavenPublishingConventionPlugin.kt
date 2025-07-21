import my.takealook.memento.configureMavenPublish
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class MavenPublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.vanniktech.maven.publish")
            configureMavenPublish()
        }
    }
}