package my.takealook.memento

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureComposeMultiplatform() {
    val compose = ComposePlugin.Dependencies(this@configureComposeMultiplatform)
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.apply {
            androidMain.dependencies {

            }

            commonMain.dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }

            iosMain.dependencies {

            }
        }
    }
    extensions.configure<ComposeExtension>() {
        extensions.configure<ResourcesExtension> {
            publicResClass = true
            generateResClass = always
        }
    }
}