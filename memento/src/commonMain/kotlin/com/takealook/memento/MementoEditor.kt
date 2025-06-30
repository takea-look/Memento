package com.takealook.memento

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.takealook.memento.resources.Res
import com.takealook.memento.resources.ic_sticker
import com.takealook.memento.sticker.Key
import com.takealook.memento.sticker.MementoStickerBuilder
import com.takealook.memento.sticker.MementoStickerListSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MementoEditor(
    modifier: Modifier = Modifier,
    stateHolder: MementoStateHolder = rememberMementoStateHolder(),
    stickerBuilder: MementoStickerBuilder<@Composable LazyGridItemScope.() -> Unit>.() -> Unit = {},
) {
    val components by stateHolder
        .state
        .collectAsStateWithLifecycle()

    val showStickerSheet by stateHolder
        .isStickerSheetOpened
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            MementoButton(
                modifier = Modifier.size(50.dp),
                onClick = stateHolder::openStickerSheet,
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
                    is MementoState.Image -> {
                        AsyncImage(
                            modifier = Modifier.offset { IntOffset(it.offsetX.toInt(), it.offsetY.toInt()) }
                                .graphicsLayer {
                                    rotationZ = it.rotation
                                    transformOrigin = TransformOrigin.Center
                                }
                                .scale(it.scale)
                                .pointerInput(Unit) {
                                    detectTransformGestures { _, pan, zoom, rotationDelta ->
                                        val updatedRotation = stateHolder.updateRotation(it.id, rotationDelta)

                                        val adjustedPan = getRotatedPan(pan, updatedRotation)
                                        stateHolder.updateLayout(it.id, adjustedPan)

                                        stateHolder.updateScale(it.id, zoom)
                                    }
                                },
                            model = Key(it.key),
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }
    }

    if (showStickerSheet) {
        MementoStickerListSheet(
            onDismiss = stateHolder::closeStickerSheet,
            builder = stickerBuilder
        )
    }
}

@Preview
@Composable
fun MementoEditorPreview() {
    MementoEditor()
}
