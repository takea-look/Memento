package com.takealook.memento

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takealook.memento.resources.Res
import com.takealook.memento.resources.ic_sticker
import com.takealook.memento.sticker.MementoStickerBuilder
import com.takealook.memento.sticker.MementoStickerListSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MementoEditor(
    modifier: Modifier = Modifier,
    stateHolder: MementoStateHolder = rememberMementoStateHolder(),
    stickerBuilder: MementoStickerBuilder.() -> Unit = {},
) {
    val components by stateHolder
        .state
        .collectAsStateWithLifecycle()

    var showStickerSheet by remember { mutableStateOf(false) }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            MementoButton(
                modifier = Modifier.size(50.dp),
                onClick = { showStickerSheet = true },
                icon = Res.drawable.ic_sticker,
                contentDescription = "sticker icon"
            )
        }

        components.forEach {
            key(it.id) {
                when (it) {
                    is MementoState.Text -> {
                        MementoTextField(
                            state = it,
                            onZoom = { zoom ->
                                stateHolder.updateScale(it.id, zoom)
                            },
                            onDrag = { dragAmount ->
                                stateHolder.updateLayout(it.id, dragAmount)
                            },
                            onRotate = { rotationDelta ->
                                stateHolder.updateRotation(it.id, rotationDelta)
                            }
                        )
                    }
                    else -> {} // TODO : Must be implemented
                }
            }
        }
    }

    if (showStickerSheet) {
        MementoStickerListSheet(
            onDismiss = { showStickerSheet = false },
            builder = stickerBuilder
        )
    }
}

@Preview
@Composable
fun MementoEditorPreview() {
    MementoEditor()
}
