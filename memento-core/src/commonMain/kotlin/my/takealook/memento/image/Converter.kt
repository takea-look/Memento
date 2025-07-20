package my.takealook.memento.image

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Converts a [ByteArray] to an [ImageBitmap].
 *
 * This function takes a byte array representing image data and attempts to decode it
 * into an `ImageBitmap` object, which can be used for display in Compose UI.
 * The specific image formats supported may depend on the underlying platform implementation.
 *
 * @return An [ImageBitmap] created from the byte array.
 * @throws IllegalArgumentException if the byte array cannot be decoded into a valid image.
 */
expect fun ByteArray.asImageBitmap(): ImageBitmap