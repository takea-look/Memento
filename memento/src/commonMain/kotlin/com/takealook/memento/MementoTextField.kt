package com.takealook.memento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp

@Composable
fun MementoTextField(
    state: MementoState.Text,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
) {
    val textState = rememberTextFieldState(state.text)
    val baseFontSize = textStyle.fontSize.takeIf(TextUnit::isSpecified) ?: 50.sp
    val scaledFontSize = (baseFontSize.value * state.scale).sp

    Box(modifier = modifier) {
        BasicTextField(
            state = textState,
            textStyle = textStyle.copy(fontSize = scaledFontSize, background = Color.Red),
            modifier = Modifier.wrapContentWidth()
        )
    }
}
