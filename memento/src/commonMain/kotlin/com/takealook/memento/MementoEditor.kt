package com.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.takealook.memento.resources.Res
import com.takealook.memento.resources.ic_sticker
import com.takealook.memento.sticker.Key
import com.takealook.memento.sticker.MementoStickerBuilder
import com.takealook.memento.sticker.MementoStickerListSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

internal val focusOffset = Offset(x = 50.dp.value, y = 500.dp.value)

@Composable
internal fun MementoEditor(
    modifier: Modifier = Modifier,
    stateHolder: MementoStateHolder = rememberMementoStateHolder(),
    mainContent: @Composable BoxScope.() -> Unit = {},
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

    var requestText by rememberSaveable { mutableStateOf(false) }

    val newTextState = rememberTextFieldState("", TextRange(12321321))
    val newTextFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(newTextState) {
        if (newTextState.text.isEmpty()) {
            newTextState.setTextAndPlaceCursorAtEnd("")
        }
    }

    LaunchedEffect(requestText) {
        if (requestText) {
            newTextFocusRequester.requestFocus()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    val focused = components.lastOrNull() ?: return@detectTransformGestures
                    stateHolder.updateRotation(focused.id, rotationDelta)

                    stateHolder.updateLayout(focused.id, pan)

                    stateHolder.updateScale(focused.id, zoom)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        requestText = true
                        stateHolder.requestFocusMode(true)
                    }
                )
            }
    ) {
        // Main Content Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            content = mainContent
        )

        // Tools List
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .zIndex(998F)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            MementoButton(
                modifier = Modifier.size(50.dp),
                onClick = stateHolder::openStickerSheet,
                icon = Res.drawable.ic_sticker,
                contentDescription = "sticker icon"
            )
        }

        // TextField for Edit Mode
        if (requestText) {
            MementoTextField(
                state = newTextState,
                modifier = Modifier
                    .offset { IntOffset(x = focusOffset.x.toInt(), y = focusOffset.y.toInt()) }
                    .zIndex(1000F)
                    .focusRequester(focusRequester = newTextFocusRequester),
                textStyle = TextStyle.Default.copy(
                    fontSize = 50.sp,
                    background = Color.Red
                )
            )
        }

        // Components
        components.forEachIndexed { index, it ->
            key(it.id) {
                when (it) {
                    is MementoState.Text -> {
                        val zIndex = if (isTextFocused && stateHolder.focusId == it.id) 1000F else 0f
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
                            },
                            onKeyDown = {
                                stateHolder.bringToFront(it.id)
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
                modifier = Modifier.zIndex(999F),
                onTouchOutSide = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    if (requestText) {
                        requestText = false
                        stateHolder.requestFocusMode(false)
                        if (newTextState.text.isNotEmpty()) {
                            stateHolder.createText(
                                focusOffset,
                                initialText = newTextState.text.toString()
                            )
                        }
                        newTextFocusRequester.freeFocus()
                        newTextState.clearText()
                    } else {
                        stateHolder.releaseFocus()
                    }
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
    )
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
