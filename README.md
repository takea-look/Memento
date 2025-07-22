# Memento

[![Maven Central](https://img.shields.io/maven-central/v/my.takealook.memento/memento-ui.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=g:my.takealook.memento)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

Memento is a powerful and customizable image editor library for Kotlin Multiplatform, inspired by Instagram Stories. It allows you to effortlessly add, edit, and manipulate text and images on a canvas, all with intuitive gestures.

Built with Jetpack Compose for Multiplatform, Memento provides a seamless experience on both Android and iOS.

## ✨ Features

- **Multiplatform:** Write your UI logic once and run it on both Android and iOS.
- **Gesture-driven:** Easily move, scale, and rotate elements with familiar touch gestures.
- **Text Editing:** Add and customize text with various colors and styles.
- **Image Overlays:** Add images or stickers on top of your main content.
- **Layer Management:** Control the stacking order of different elements.
- **State Management:** A robust `MementoController` to manage the state of your creations.
- **Capture & Export:** Capture the final edited canvas as an `ImageBitmap`.

## 🎬 Demo

*(A GIF or screenshot demonstrating the editor in action would be perfect here!)*

![Memento Demo](https://user-images.githubusercontent.com/12345/67890.gif)

## 📦 Download

Memento is available on **Maven Central**.

1.  Make sure you have `mavenCentral()` in your top-level `build.gradle.kts` file:
    ```kotlin
    repositories {
        mavenCentral()
    }
    ```

2.  Add the desired dependency to your module's `build.gradle.kts` file:

    ```kotlin
    dependencies {
        // For the full UI editor experience (includes core)
        implementation("my.takealook.memento:memento-ui:0.0.1")

        // Or if you only need the core logic
        implementation("my.takealook.memento:memento-core:0.0.1")
    }
    ```
    *Always check for the [latest version](https://central.sonatype.com/search?q=g:my.takealook.memento) on Maven Central.*

## 🚀 Basic Usage

Getting started with the `MementoEditor` is simple. Just add the composable to your UI:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.takealook.memento.MementoEditor
import my.takealook.memento.rememberMementoController

@Composable
fun MyEditorScreen() {
    val mementoController = rememberMementoController()

    MementoEditor(
        modifier = Modifier.fillMaxSize(),
        stateHolder = mementoController,
        onImageCaptured = { imageBitmap ->
            // Handle the captured image
        },
        mainContent = {
            // Your main content goes here, e.g., an Image
            Image(
                painter = painterResource(Res.drawable.my_background_image),
                contentDescription = "Background"
            )
        }
    )

    // You can control the editor externally
    Button(onClick = { mementoController.requestCapture() }) {
        Text("Save Image")
    }
}
```

## 🧩 Modules

-   **`memento-core`**: Contains the core logic, state management (`MementoController`, `MementoState`), and data classes. It's UI-agnostic.
-   **`memento-ui`**: Provides the `MementoEditor` Composable and other UI components built on `memento-core` for a ready-to-use editor experience.

## 📜 License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE.txt](LICENSE.txt) file for details.
