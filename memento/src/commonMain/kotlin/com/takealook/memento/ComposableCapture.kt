package com.takealook.memento

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import kotlinx.coroutines.launch

internal fun Modifier.capture(
    isCaptureRequested: Boolean,
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
                    val bitmapResult = graphicsLayer.toImageBitmap()
                    onCaptured(bitmapResult)
                }
            } else {
                drawContent()
            }
        }
    }
}
