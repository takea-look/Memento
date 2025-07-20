import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.memento.kotlin.multiplatform.shared)
    alias(libs.plugins.memento.compose.multiplatform.shared)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MementoUi"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coil.compose)
            implementation(libs.materialKolor)

            api(projects.mementoCore)
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "my.takealook.memento.ui.resources"
    generateResClass = auto
}

