package my.takealook.memento

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Crops an [ImageBitmap] to the specified [cropRect].
 *
 * @param source The source [ImageBitmap] to crop.
 * @param cropRect The [Rect] defining the area to crop.
 * @param density The display density, used for scaling if necessary (though not explicitly used in this Android implementation).
 * @return A new [ImageBitmap] representing the cropped area.
 */
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
