package my.takealook.memento

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
const val DEFAULT_APPLICATION_ID = "my.takealook.memento"
internal fun Project.configureKotlinMultiplatform() {

    extensions.configure<KotlinMultiplatformExtension> {
        iosX64()
        iosArm64()
        iosSimulatorArm64()

        sourceSets.apply {
            androidMain.dependencies {

            }

            commonMain.dependencies {

            }

            commonTest.dependencies {
                val test = libs.findLibrary("kotlin-test").get()
                implementation(test)
            }

        }
    }

    extensions.configure<LibraryExtension> {
        namespace = getActualNameSpaces()
        compileSdk = 35
        defaultConfig {
            minSdk = 24
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

fun Project.getActualNameSpaces() = projectDir
    .toString()
    .replace(rootDir.toString(), "")
    .replace("/", ".")
    .replace("-", "")
    .let { "$DEFAULT_APPLICATION_ID$it" }