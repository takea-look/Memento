package com.takealook.memento

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MementoEditor(
    modifier: Modifier = Modifier,
    stateHolder: MementoStateHolder = rememberMementoStateHolder()
) {
    val components by stateHolder
        .state
        .collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        stateHolder.createText(tapOffset)
                    }
                )
            }
    ) {
        components.forEach {
            key(it.id) {
                when (it) {
                    is MementoState.Text -> {
                        val state = rememberTextFieldState(it.text)
                        BasicTextField(
                            state = state,
                            modifier = Modifier
                                .offset { IntOffset(it.offsetX.toInt(), it.offsetY.toInt()) }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            // TODO : Must be implemented
                                        }
                                    )
                                }
                        )
                    }
                    else -> {} // TODO : Must be implemented
                }
            }
        }
    }
}

@Preview
@Composable
fun MementoEditorPreview() {
    MementoEditor()
}