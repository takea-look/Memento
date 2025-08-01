# Memento

[![Maven Central](https://img.shields.io/maven-central/v/my.takealook.memento/memento-ui.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=g:my.takealook.memento)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Swift Package Manager](https://img.shields.io/badge/Swift_Package_Manager-supported-red)](https://www.swift.org/documentation/package-manager/)
[![iOS](https://img.shields.io/badge/iOS-14+-red)](https://www.apple.com/kr/os/ios/)

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

![ScreenRecording_07-23-2025 00-40-30_1](https://github.com/user-attachments/assets/df57b380-d7d6-48f3-b337-bc2d3f160f0c)


## 📦 Download for Android & KMP Projects

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
        implementation("my.takealook.memento:memento-ui:<latest-version>")

        // Or if you only need the core logic
        implementation("my.takealook.memento:memento-core:<latest-version>")
    }
    ```
    *Always check for the [latest version](https://central.sonatype.com/search?q=g:my.takealook.memento) on Maven Central.*

### 🚀 Basic Usage

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
        controller = mementoController,
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

### 🧩 Modules

-   **`memento-core`**: Contains the core logic, state management (`MementoController`, `MementoState`), and data classes. It's UI-agnostic.
-   **`memento-ui`**: Provides the `MementoEditor` Composable and other UI components built on `memento-core` for a ready-to-use editor experience.

## 📦 Download for iOS Projects (via SPM)

Memento is also available on **Swift Package Manager**.

### Installation

You can add `MementoUI` to your Xcode project as a Swift Package.

1.  In Xcode, go to `File > Add Packages...`
2.  Enter the repository URL: `https://github.com/easternkite/memento.git`
3.  Choose the `MementoUi` product.

### 🚀 Basic Usage

Here's a basic example of how to integrate `MementoUI` into your SwiftUI application.

First, ensure you import `MementoUi` in your Swift files:

```swift
import SwiftUI
import UIKit
import MementoUi

struct ComposeView: UIViewControllerRepresentable {
    var controller : MementoController!
    init(_ controller: MementoController) {
        self.controller = controller
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
         return MementoEditorViewControllerKt.MementoEditorViewController(
            mainContent: UIImage(named: "ic_milk")!,
            controller: controller,
            onImageCaptured: { image in saveImageToGallery(image) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State var controller: MementoController = MementoController()
    var body: some View {
        ZStack(alignment: .topTrailing) {
            ComposeView(controller)
                    .ignoresSafeArea(.all) // Compose has own keyboard handler
            
            //...
        }
    }
}
```

### Important: `CADisableMinimumFrameDurationOnPhone`

For optimal performance and smooth animations, especially on iOS devices, it is crucial to add the `CADisableMinimumFrameDurationOnPhone` key to your application's `Info.plist` file and set its value to `YES` (Boolean).

This setting disables a system-imposed minimum frame duration, allowing your app to render at higher frame rates when possible, which is particularly beneficial for UI-intensive applications like image editors.

**How to add it:**

1.  Open your `Info.plist` file in Xcode.
2.  Right-click on an empty area in the property list editor and choose `Add Row`.
3.  For the `Key`, enter `CADisableMinimumFrameDurationOnPhone`.
4.  For the `Type`, select `Boolean`.
5.  For the `Value`, set it to `YES`.

Alternatively, you can add the following XML snippet directly to your `Info.plist` file:

```xml
<key>CADisableMinimumFrameDurationOnPhone</key>
<true/>
```


## 📜 License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE.txt](LICENSE.txt) file for details.
