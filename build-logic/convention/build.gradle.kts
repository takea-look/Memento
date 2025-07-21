import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "my.takealook.memento.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.multiplatformPlugin)
    compileOnly(libs.maven.publishPlugin)
}

gradlePlugin {
    plugins {
        create("memento.kotlin.multiplatform.shared") {
            id = "memento.kotlin.multiplatform.shared"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        create("memento.compose.multiplatform.shared") {
            id = "memento.compose.multiplatform.shared"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        create("memento.maven.publish") {
            id = "memento.maven.publish"
            implementationClass = "MavenPublishingConventionPlugin"
        }
    }
}