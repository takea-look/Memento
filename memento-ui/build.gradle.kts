@file:OptIn(ExperimentalSwiftExportDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl

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

    val xcFrameworkName = "MementoUi"
    val xcf = XCFramework(xcFrameworkName)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            binaryOption("bundleId", "my.takealook.memento.ui")
            baseName = xcFrameworkName
            isStatic = true
            export(projects.mementoCore)
            xcf.add(this)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.materialKolor)
            implementation(libs.coil.compose)
            api(projects.mementoCore)
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "my.takealook.memento.ui.resources"
    generateResClass = auto
}

