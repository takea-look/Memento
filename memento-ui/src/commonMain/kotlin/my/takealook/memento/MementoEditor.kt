package my.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Offset for the new text field when it's focused.
 * This is used to position the text field in the center of the screen.
 * The Y value is set to a large value to ensure that the text field is
 * not visible when it's not focused.
 */
internal val focusOffset = Offset(x = 50.dp.value, y = 500.dp.value)

/**
 * Composable function for the Memento Editor.
 * This editor allows users to add and manipulate text and images on a canvas.
 *
 * @param modifier Modifier to be applied to the editor.
 * @param controller The state holder for managing the editor's state.
 * @param onImageCaptured Callback function that is invoked when an image is captured.
 * @param mainContent The main content to be displayed in the editor, typically an image.
 */
@Composable
fun MementoEditor(
    modifier: Modifier = Modifier,
    controller: MementoController = rememberMementoController(),
    onImageCaptured: (ImageBitmap) -> Unit = {},
    mainContent: @Composable BoxScope.() -> Unit = {},
) {
    val components by controller
        .state
        .collectAsStateWithLifecycle()

    val isTextFocused by controller
        .isTextFocused
        .collectAsStateWithLifecycle()

    val isCaptureRequested by controller
        .isCaptureRequested
        .collectAsStateWithLifecycle()

    var requestText by rememberSaveable { mutableStateOf(false) }

    var textSeedColor by remember { mutableStateOf(Color.Unspecified) }
    val newTextState = rememberTextFieldState("", TextRange(12321321))
    val newTextFocusRequester = remember { FocusRequester() }
    val newTextColorScheme = remember(textSeedColor) {
        getMementoColorScheme(textSeedColor)
    }
    var newTextOffset by remember { mutableStateOf(Offset.Zero) }

    var imageRect : Rect? by remember { mutableStateOf(null) }
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
            .capture(
                isCaptureRequested = isCaptureRequested,
                cropRect = imageRect
            ) {
                onImageCaptured(it)
                controller.finishCapture()
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    val focused = components.lastOrNull() ?: return@detectTransformGestures
                    controller.updateRotation(focused.id, rotationDelta)

                    controller.updateLayout(focused.id, pan)

                    controller.updateScale(focused.id, zoom)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        requestText = true
                        controller.requestFocusMode(true)
                    }
                )
            }
    ) {
        // TextField for Edit Mode
        if (requestText) {
            MementoTextField(
                state = newTextState,
                modifier = Modifier
                    .offset { IntOffset(focusOffset.x.toInt(), focusOffset.y.toInt()) }
                    .onGloballyPositioned {
                        newTextOffset = it.positionInParent()
                    }
                    .zIndex(1000F)
                    .focusRequester(focusRequester = newTextFocusRequester),
                textStyle = TextStyle.Default.copy(
                    color = newTextColorScheme.primary,
                    fontSize = 24.sp,
                    background = newTextColorScheme.primaryContainer,
                )
            )
        }

        // Main Content Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .onGloballyPositioned {
                    imageRect = it.boundsInParent()
                }
            ,
            content = mainContent
        )

        if (isTextFocused) {
            FocusModeScreen(
                modifier = Modifier.zIndex(999F),
                onTouchOutSide = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    if (requestText) {
                        requestText = false
                        controller.requestFocusMode(false)
                        if (newTextState.text.isNotEmpty()) {
                            controller.createText(
                                Offset(newTextOffset.x, newTextOffset.y),
                                initialText = newTextState.text.toString(),
                                seedColor = textSeedColor
                            )
                        }

                        newTextFocusRequester.freeFocus()
                        newTextState.clearText()
                    } else {
                        controller.releaseFocus()
                    }
                }
            )

            MementoRainbowPalette(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1001F)
                    .imePadding(),
                onColorClick = {
                    if (controller.focusId != null) {
                        controller.updateText(controller.focusId!!, it)
                    }
                    textSeedColor = it
                }
            )
        }
        // Components
        components.forEachIndexed { index, it ->
            key(it.id) {
                when (it) {
                    is MementoState.Text -> {
                        val isTextFieldFocused = isTextFocused && controller.focusId == it.id
                        val zIndex = if (isTextFieldFocused) 1000F else 0f

                        MementoTextField(
                            modifier = Modifier
                                .zIndex(zIndex)
                                .mementoGesture(it, controller),
                            state = it,
                            onFocused = {
                                val paddingTop = 16.dp.value
                                val savedOffset = Offset(it.offsetX, it.offsetY - paddingTop)
                                controller.executeTextFocus(
                                    it.id,
                                    savedOffset,
                                    it.rotation,
                                    it.scale
                                )
                            },
                            onKeyDown = {
                                controller.bringToFront(it.id)
                            }
                        )
                    }

                    is MementoState.Image -> {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .mementoGesture(it, controller)
                                .scale(it.scale),
                            content = { it.content() }
                        )
                    }
                }
            }
        }

        // Tools List
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .zIndex(998F)
        ) {
        }


    }
}

/**
 * A composable function that displays a semi-transparent overlay to indicate focus mode.
 * This screen dims the background and allows interaction only with the focused element.
 *
 * @param modifier The modifier to be applied to the FocusModeScreen.
 * @param onTouchOutSide A callback function that is invoked when the user touches outside the focused area.
 *                       This is typically used to exit focus mode.
 */
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
            .clickable(
                interactionSource = null,
                enabled = true,
                onClick = onTouchOutSide,
                indication = null
            )
    )
}
