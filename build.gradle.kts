plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.memento.kotlin.multiplatform.shared) apply false
    alias(libs.plugins.memento.compose.multiplatform.shared) apply false
    alias(libs.plugins.memento.maven.publish) apply false
}