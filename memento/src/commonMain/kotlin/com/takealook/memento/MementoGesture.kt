package com.takealook.memento

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

internal fun Modifier.mementoGesture(
    state: MementoState,
    holder: MementoStateHolder
) = composed {
    val animatedOffset = remember {
        Animatable(
            Offset(
                state.offsetX,
                state.offsetY
            ), Offset.VectorConverter
        )
    }

    val isFocused by holder.isTextFocused.collectAsStateWithLifecycle()

    // TODO: Implement Rotate, Scale Animation here

    LaunchedEffect(isFocused) {
        animatedOffset.animateTo(
            targetValue = Offset(state.offsetX, state.offsetY),
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(state.offsetX, state.offsetY) {
        if (!isFocused && animatedOffset.targetValue != Offset(state.offsetX, state.offsetY)) {
            animatedOffset.snapTo(Offset(state.offsetX, state.offsetY))
        }
    }

    this
        .offset {
            IntOffset(
                animatedOffset.value.x.roundToInt(),
                animatedOffset.value.y.roundToInt()
            )
        }
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
