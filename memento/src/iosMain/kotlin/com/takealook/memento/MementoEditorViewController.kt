package com.takealook.memento

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import coil3.compose.AsyncImage
import platform.UIKit.UIImage

@OptIn(ExperimentalMaterial3Api::class)
fun MementoEditorViewController(
    mainContent: UIImage,
) = ComposeUIViewController {
    val holder = rememberMementoStateHolder()

    MementoEditor(
        stateHolder = holder,
        mainContent = {
            AsyncImage(
                model = mainContent.getBytes(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxSize(),
        onImageCaptured = { /** TODO : must image capture logic be implemented */ }
    )
}
