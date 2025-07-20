package my.takealook.memento

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

/**
 * Crops the given [source] ImageBitmap to the specified [cropRect].
 *
 * This function creates a new [ImageBitmap] containing only the pixels
 * within the [cropRect] of the original [source] image.
 *
 * The [density] parameter is currently unused in this iOS implementation but is included
 * for API consistency with other platforms.
 *
 * @param source The [ImageBitmap] to be cropped.
 * @param cropRect The [Rect] defining the area to crop from the [source] image.
 *                 Coordinates are relative to the top-left of the [source] image.
 *                 The dimensions of the crop will be clamped to the bounds of the [source] image.
 * @param density The display density. Currently unused in this implementation.
 * @return A new [ImageBitmap] representing the cropped portion of the [source] image.
 */
actual fun cropImageBitmap(
    source: ImageBitmap,
    cropRect: Rect,
    density: Float
): ImageBitmap {
    val x = cropRect.left.toInt().coerceIn(0, source.width)
    val y = cropRect.top.toInt().coerceIn(0, source.height)
    val width = cropRect.width.toInt().coerceAtMost(source.width - x)
    val height = cropRect.height.toInt().coerceAtMost(source.height - y)

    val croppedBitmap = ImageBitmap(width, height, ImageBitmapConfig.Argb8888)

    val canvas = Canvas(croppedBitmap)

    canvas.drawImageRect(
        image = source,
        srcOffset = IntOffset(x, y),
        srcSize = IntSize(width, height),
        dstOffset = IntOffset(0, 0),
        dstSize = IntSize(width, height),
        paint = Paint()
    )

    return croppedBitmap
}
