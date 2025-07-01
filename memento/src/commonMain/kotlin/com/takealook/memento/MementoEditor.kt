package com.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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

    val isTextFocused by stateHolder
        .isTextFocused
        .collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
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

        components.forEachIndexed { index, it ->
            key(it.id) {
                when (it) {
                    is MementoState.Text -> {
                        val zIndex = if (isTextFocused && stateHolder.focusId == it.id) 100F else 0f
                        MementoTextField(
                            modifier = Modifier
                                .zIndex(zIndex)
                                .mementoGesture(it, stateHolder),
                            state = it,
                            onFocused = {
                                val paddingTop = 16.dp.value
                                val savedOffset = Offset(it.offsetX, it.offsetY - paddingTop)
                                stateHolder.executeTextFocus(
                                    it.id,
                                    savedOffset,
                                    it.rotation,
                                    it.scale
                                )
                            }
                        )
                    }

                    is MementoState.Image -> {
                        AsyncImage(
                            modifier = Modifier
                                .mementoGesture(it, stateHolder)
                                .scale(it.scale),
                            model = Key(it.key),
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }

        if (isTextFocused) {
            FocusModeScreen(
                onTouchOutSide = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    stateHolder.releaseFocus()
                }
            )
        }
    }

    if (showStickerSheet) {
        MementoStickerListSheet(
            onDismiss = stateHolder::closeStickerSheet,
            builder = stickerBuilder
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FocusModeScreen(
    modifier: Modifier = Modifier,
    onTouchOutSide: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(enabled = true, onClick = onTouchOutSide)
    ) {
        Text("완료", modifier = Modifier.align(Alignment.TopEnd))
    }
}

@Preview
@Composable
fun FocusModeScreenPreview() {
    FocusModeScreen(
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun MementoEditorPreview() {
    MementoEditor()
}
