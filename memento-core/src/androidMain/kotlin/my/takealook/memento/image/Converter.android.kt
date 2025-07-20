package my.takealook.memento.image

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Converts a ByteArray to an ImageBitmap.
 *
 * This function decodes the ByteArray into a Bitmap using BitmapFactory
 * and then converts the Bitmap to an ImageBitmap.
 *
 * @return The ImageBitmap representation of the ByteArray.
 */
actual fun ByteArray.asImageBitmap(): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return bitmap.asImageBitmap()
}