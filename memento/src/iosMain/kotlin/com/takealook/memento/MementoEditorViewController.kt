package com.takealook.memento

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import platform.UIKit.UIImage

@OptIn(ExperimentalMaterial3Api::class)
fun MementoEditorViewController(
    mainContent: UIImage,
    stateHolder: MementoStateHolder,
    onImageCaptured: (UIImage) -> Unit
) = ComposeUIViewController {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .logger(DebugLogger())
            .crossfade(true)
            .build()
    }

    MementoEditor(
        stateHolder = stateHolder,
        mainContent = {
            AsyncImage(
                model = mainContent.getBytes(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxSize(),
        onImageCaptured = {
            val uiImage = it.toUIImage()
            if (uiImage != null) {
                onImageCaptured(uiImage)
            }
        }
    )
}
