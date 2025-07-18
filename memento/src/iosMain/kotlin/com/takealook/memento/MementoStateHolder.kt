package com.takealook.memento

import coil3.compose.AsyncImage
import platform.UIKit.UIImage

fun MementoStateHolder.attachImage(
    image: UIImage,
    contentDescription: String?
) {
    val bytes = image.getBytes()
    attachImage {
        AsyncImage(
            model = bytes,
            contentDescription = contentDescription
        )
    }
}
