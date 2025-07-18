package com.takealook.memento

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun cropImageBitmap(
    source: ImageBitmap,
    cropRect: Rect,
    density: Float
): ImageBitmap {
    val sourceBitmap = source.asAndroidBitmap()
    return Bitmap.createBitmap(
        sourceBitmap,
        cropRect.left.toInt(),
        cropRect.top.toInt(),
        cropRect.width.toInt(),
        cropRect.height.toInt()
    ).asImageBitmap()
}
