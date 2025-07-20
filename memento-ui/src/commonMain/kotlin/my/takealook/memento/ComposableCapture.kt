package my.takealook.memento

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import kotlinx.coroutines.launch

/**
 * Captures the content of a Composable as an [ImageBitmap].
 *
 * This modifier uses a [GraphicsLayer] to render the Composable's content off-screen
 * when [isCaptureRequested] is true. The captured image is then cropped according to
 * the provided [cropRect] (if not null) and passed to the [onCaptured] callback.
 *
 * Note: The capture process is asynchronous. The [onCaptured] callback will be invoked
 * on a coroutine launched within the composition's [rememberCoroutineScope].
 *
 * Important: If [isCaptureRequested] is true, [cropRect] must not be null, otherwise
 * a [NullPointerException] will occur.
 *
 * @param isCaptureRequested A boolean flag indicating whether a capture should be performed.
 *                           When true, the Composable's content will be drawn to a [GraphicsLayer]
 *                           and an [ImageBitmap] will be generated.
 * @param cropRect An optional [Rect] defining the area to crop from the captured image.
 *                 If null, the entire captured image is returned.
 *                 **Must not be null if [isCaptureRequested] is true.**
 * @param onCaptured A callback function that will be invoked with the captured (and potentially cropped)
 *                   [ImageBitmap] when the capture is complete.
 */
internal fun Modifier.capture(
    isCaptureRequested: Boolean,
    cropRect: Rect?,
    onCaptured: (ImageBitmap) -> Unit
) = composed {
    val graphicsLayer = rememberGraphicsLayer()
    val scope = rememberCoroutineScope()
    this.drawWithCache {
        onDrawWithContent {
            if (isCaptureRequested) {
                graphicsLayer.record {
                    this@onDrawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)

                scope.launch {
                    val fullBitmap = graphicsLayer.toImageBitmap()
                    val croppedBitmap = cropImageBitmap(
                        fullBitmap,
                        cropRect!!,
                        this@onDrawWithContent.density
                    )

                    onCaptured(croppedBitmap)
                }
            } else {
                drawContent()
            }
        }
    }
}

/**
 * Crops an [ImageBitmap] to the specified [cropRect].
 *
 * This function is platform-specific and its implementation will vary
 * depending on the target platform (e.g., Android, iOS, Desktop).
 *
 * @param source The original [ImageBitmap] to be cropped.
 * @param cropRect The [Rect] defining the area to crop from the source image.
 *                 The coordinates in the [Rect] are relative to the source image.
 * @param density The density of the display, used for accurate pixel scaling.
 * @return A new [ImageBitmap] representing the cropped portion of the source image.
 */
expect fun cropImageBitmap(
    source: ImageBitmap,
    cropRect: Rect,
    density: Float
): ImageBitmap
