package com.takealook.memento

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

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
