package my.takealook.memento

import coil3.compose.AsyncImage
import my.takealook.memento.image.getBytes
import platform.UIKit.UIImage

/**
 * Attaches an image to the memento.
 *
 * This function takes a [UIImage] and an optional [contentDescription] and
 * converts the image to a byte array. It then uses the `attachImage` function
 * (presumably from the `MementoStateHolder` class) to display the image
 * using `AsyncImage`.
 *
 * @param image The [UIImage] to be attached.
 * @param contentDescription An optional description of the image for accessibility.
 */
fun MementoController.attachImage(
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
