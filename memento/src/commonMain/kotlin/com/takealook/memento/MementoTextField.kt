package com.takealook.memento

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp

@Composable
fun MementoTextField(
    state: MementoState.Text,
    onDrag: (dragAmount: Offset) -> Unit,
    onZoom: (zoom: Float) -> Unit,
    onRotate: (rotationDelta: Float) -> Float,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
) {
    val textState = rememberTextFieldState(state.text)
    val baseFontSize = textStyle.fontSize.takeIf(TextUnit::isSpecified) ?: 50.sp
    val scaledFontSize = (baseFontSize.value * state.scale).sp

    Box(
        modifier = modifier
            .offset { IntOffset(state.offsetX.toInt(), state.offsetY.toInt()) }
            .graphicsLayer {
                rotationZ = state.rotation
                transformOrigin = TransformOrigin.Center
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    val updatedRotation = onRotate(rotationDelta)

                    val adjustedPan = getRotatedPan(pan, updatedRotation)
                    onDrag(adjustedPan)

                    onZoom(zoom)
                }
            }
    ) {
        BasicTextField(
            state = textState,
            textStyle = textStyle.copy(fontSize = scaledFontSize, background = Color.Red),
            modifier = Modifier.wrapContentWidth()
        )
    }
}
