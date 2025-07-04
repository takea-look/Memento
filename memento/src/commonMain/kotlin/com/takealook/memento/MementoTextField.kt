package com.takealook.memento

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp

@Composable
fun MementoTextField(
    state: MementoState.Text,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onFocused: () -> Unit = {},
    onKeyDown: () -> Unit = {},
) {
    val baseFontSize = textStyle.fontSize.takeIf(TextUnit::isSpecified) ?: 50.sp
    val scaledFontSize = (baseFontSize.value * state.scale).sp
    val textStyle = textStyle.copy(fontSize = scaledFontSize, background = Color.Red)
    val textState = rememberTextFieldState(state.text)

    MementoTextField(
        state = textState,
        textStyle = textStyle,
        onFocused = onFocused,
        onKeyDown = onKeyDown,
        modifier = modifier
    )
}

@Composable
fun MementoTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onFocused: () -> Unit = {},
    onKeyDown: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Press) {
                onKeyDown()
            }
        }
    }

    Box(modifier = modifier) {
        BasicTextField(
            state = state,
            textStyle = textStyle,
            interactionSource = interactionSource,
            modifier = Modifier
                .preventAutoWrap()
                .width(IntrinsicSize.Min)
                .defaultMinSize(minWidth = 10.dp)
                .onFocusChanged {
                    if (it.isFocused) {
                        onFocused()
                    }
                },
            cursorBrush = SolidColor(Color.Black)
        )
    }
}

fun Modifier.preventAutoWrap(): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = Constraints.Infinity
        )
    )

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}