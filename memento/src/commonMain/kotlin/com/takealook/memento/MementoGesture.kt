package com.takealook.memento

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset

internal fun Modifier.mementoGesture(
    state: MementoState,
    holder: MementoStateHolder
) = composed {
    this
        .offset { IntOffset(state.offsetX.toInt(), state.offsetY.toInt()) }
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, rotationDelta ->
                holder.updateRotation(state.id, rotationDelta)

                holder.updateLayout(state.id, pan)

                holder.updateScale(state.id, zoom)
            }
        }.graphicsLayer {
            rotationZ = state.rotation
            transformOrigin = TransformOrigin.Center
        }
}
