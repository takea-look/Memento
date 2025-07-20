package my.takealook.memento.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

/**
 * Converts a [ByteArray] to an [ImageBitmap].
 *
 * This function decodes the byte array into an image using Skia's `Image.makeFromEncoded`
 * and then converts it to a Compose `ImageBitmap`.
 *
 * @return The [ImageBitmap] representation of the byte array.
 * @throws IllegalArgumentException if the byte array cannot be decoded into an image.
 */
actual fun ByteArray.asImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

/**
 * Converts an [ImageBitmap] to a [UIImage].
 *
 * @return The converted [UIImage], or `null` if the conversion fails.
 */
@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val width = this.width
    val height = this.height
    val argbBuffer = IntArray(width * height)
    this.readPixels(argbBuffer)

    val rgbaBuffer = ByteArray(argbBuffer.size * 4)
    for (i in argbBuffer.indices) {
        val argb = argbBuffer[i]
        val a = (argb shr 24) and 0xFF
        val r = (argb shr 16) and 0xFF
        val g = (argb shr 8) and 0xFF
        val b = argb and 0xFF

        rgbaBuffer[i * 4 + 0] = r.toByte()
        rgbaBuffer[i * 4 + 1] = g.toByte()
        rgbaBuffer[i * 4 + 2] = b.toByte()
        rgbaBuffer[i * 4 + 3] = a.toByte()
    }

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = rgbaBuffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = (CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or 0u)
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}

/**
 * Converts a UIImage to a ByteArray.
 *
 * This function takes a UIImage as input and converts it into a ByteArray.
 * It uses the PNG representation of the image for the conversion.
 *
 * @return A ByteArray representing the image, or null if the conversion fails.
 */
@OptIn(ExperimentalForeignApi::class)
fun UIImage.getBytes(): ByteArray {
    val pngRepresentation = UIImagePNGRepresentation(this)!!
    val byteArray = ByteArray(pngRepresentation.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), pngRepresentation.bytes, pngRepresentation.length)
        }
    }
    return byteArray
}