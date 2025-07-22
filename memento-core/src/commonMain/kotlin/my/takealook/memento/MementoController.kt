package my.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Controls the state of memento components, including their layout, focus, and capture requests.
 *
 * This class manages a list of [MementoState] objects, representing the current state of all
 * memento components on the screen. It provides methods for updating the layout, focus, and
 * capture state of these components, as well as saving and restoring the state of the controller.
 *
 * @property _state A [MutableStateFlow] that holds the current list of [MementoState] objects.
 * @property state A [StateFlow] that exposes the current list of [MementoState] objects as an
 * immutable stream.
 * @property _focusId The ID of the currently focused memento component, or null if no component
 * is focused.
 * @property focusId The ID of the currently focused memento component, or null if no component
 * is focused.
 * @property savedOffset The saved offset of the focused memento component, used when releasing
 * focus.
 * @property savedRotation The saved rotation of the focused memento component, used when releasing
 * focus.
 * @property savedScale The saved scale of the focused memento component, used when releasing focus.
 * @property _isTextFocused A [MutableStateFlow] that indicates whether a text memento component
 * is currently focused.
 * @property isTextFocused A [StateFlow] that exposes the current text focus state as an immutable
 * stream.
 * @property _isCaptureRequested A [MutableStateFlow] that indicates whether a capture of the
 * memento components has been requested.
 * @property isCaptureRequested A [StateFlow] that exposes the current capture request state as an
 * immutable stream.
 * @property Saver A [Saver] object that can be used to save and restore the state of the
 * [MementoController].
 */
class MementoController {
    internal val _state: MutableStateFlow<List<MementoState>> = MutableStateFlow(emptyList())
    val state = _state.asStateFlow()

    internal var _focusId: Int? = null
    val focusId: Int? get() = _focusId

    internal var savedOffset: Offset? = null
    internal var savedRotation: Float? = null
    internal var savedScale: Float? = null

    internal val _isTextFocused = MutableStateFlow(false)
    val isTextFocused = _isTextFocused.asStateFlow()

    internal val _isCaptureRequested = MutableStateFlow(false)
    val isCaptureRequested = _isCaptureRequested.asStateFlow()

    companion object Companion {
        private val TYPE = "type"
        private val ID = "id"
        private val OFFSET_X = "offsetX"
        private val OFFSET_Y = "offsetY"
        private val SCALE = "scale"
        private val ROTATION = "rotation"
        private val TEXT = "text"
        private val COLOR = "color"

        val Saver = listSaver<MementoController, Map<String, Any?>>(
            save = {
                it.state.value.map {
                    when(it) {
                        is MementoState.Text -> mapOf(
                            TYPE to "Text",
                            ID to it.id,
                            OFFSET_X to it.offsetX,
                            OFFSET_Y to it.offsetY,
                            SCALE to it.scale,
                            ROTATION to it.rotation,
                            TEXT to it.text,
                            COLOR to it.seedColor
                        )
                        is MementoState.Image -> mapOf(
                            TYPE to "Image",
                            ID to it.id,
                            OFFSET_X to it.offsetX,
                            OFFSET_Y to it.offsetY,
                            SCALE to it.scale,
                            ROTATION to it.rotation,
                        )
                    }
                }
            },
            restore = { list ->
                val mappedState = list.map {
                    when(it[TYPE]) {
                        "Text" -> MementoState.Text(
                            id = it[ID] as Int,
                            offsetX = it[OFFSET_X] as Float,
                            offsetY = it[OFFSET_Y] as Float,
                            scale = it[SCALE] as Float,
                            rotation = it[ROTATION] as Float,
                            text = it[TEXT] as String,
                            seedColor = it[COLOR] as ULong
                        )

                        "Image" -> MementoState.Image(
                            id = it[ID] as Int,
                            offsetX = it[OFFSET_X] as Float,
                            offsetY = it[OFFSET_Y] as Float,
                            scale = it[SCALE] as Float,
                            rotation = it[ROTATION] as Float,
                            content = {}
                        )
                        else -> throw IllegalArgumentException()
                    }
                }
                MementoController().apply { _state.value = mappedState }
            }
        )
    }
}

/**
 * Executes the text focus operation.
 *
 * This function saves the current offset, rotation, and scale of the memento,
 * then updates the layout of the memento with the given ID to a fixed position,
 * zero rotation, and unit scale. Finally, it requests focus mode.
 *
 * @param id The ID of the memento to focus.
 * @param currentOffset The current offset of the memento.
 * @param currentRotation The current rotation of the memento.
 * @param currentScale The current scale of the memento.
 */
fun MementoController.executeTextFocus(
    id: Int,
    currentOffset: Offset,
    currentRotation: Float,
    currentScale: Float
) {
    savedOffset = currentOffset
    savedRotation = currentRotation
    savedScale = currentScale

    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            _focusId = id
            it.updateLayout(
                offsetX = 50.dp.value,
                offsetY = 500.dp.value
            ).updateRotation(0f).updateScale(1f)
        } else it
    }

    components.clear()
    components.addAll(updatedComponents)
    _state.value = components
    requestFocusMode(true)
}

/**
 * Sets the focus mode for text elements.
 *
 * When enabled, text elements can be interacted with (e.g., edited).
 * When disabled, text elements are static.
 *
 * @param enabled True to enable focus mode, false to disable it. Defaults to true.
 */
fun MementoController.requestFocusMode(enabled: Boolean = true) {
    _isTextFocused.value = enabled
}

/**
 * Releases the focus from the currently focused Memento component.
 *
 * This function restores the original offset, rotation, and scale of the focused component
 * that were saved when `executeTextFocus` was called. It then clears the focus state,
 * effectively making no component focused.
 * If no component is currently focused (i.e., `savedOffset` is null), this function does nothing.
 */
fun MementoController.releaseFocus() {
    if (savedOffset == null) return
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == _focusId!!) {
            it.updateLayout(
                offsetX = savedOffset!!.x,
                offsetY = savedOffset!!.y
            ).updateRotation(savedRotation!!).updateScale(savedScale!!)
        } else it
    }
    savedOffset = null
    savedRotation = null
    savedScale = null
    _focusId = null
    components.clear()
    components.addAll(updatedComponents)
    _state.value = components
    _isTextFocused.value = false
}

/**
 * Brings the component with the given ID to the front of the list.
 * This ensures that the component is rendered on top of other components.
 * If no component with the given ID is found, the list remains unchanged.
 *
 * @param id The ID of the component to bring to the front.
 */
fun MementoController.bringToFront(id: Int) {
    val components = state.value.toMutableList()
    val index = components.indexOfFirst { it.id == id }
    if (index != -1) {
        val component = components.removeAt(index)
        components.add(component)
        _state.value = components
    }
}

/**
 * Requests a capture of the current memento state.
 * This function sets a flag that indicates a capture is requested.
 * The actual capture process should be handled by observing the `isCaptureRequested` Flow.
 */
fun MementoController.requestCapture() {
    _isCaptureRequested.value = true
}

/**
 * Resets the capture request flag.
 *
 * This function should be called after a capture has been successfully completed or if it needs to be cancelled.
 * It sets the internal `_isCaptureRequested` state to `false`, indicating that no capture is currently pending.
 */
fun MementoController.finishCapture() {
    _isCaptureRequested.value = false
}

/**
 * Updates a [MementoState] in the [MementoController] with the given [id].
 *
 * @param id The id of the [MementoState] to update.
 * @param onUpdate A lambda function that takes the current [MementoState] and returns the updated [MementoState].
 * @param T The type of the [MementoState] to update.
 *
 * @see MementoState
 * @see MementoController
 */
@Suppress("UNCHECKED_CAST")
fun <T : MementoState> MementoController.update(id: Int, onUpdate: (T) -> MementoState) {
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            onUpdate(it as T)
        } else it
    }
    components.clear()
    components.addAll(updatedComponents)
    _state.value = components
}

/**
 * Updates the text color of a specific MementoState.Text component.
 *
 * @param id The ID of the MementoState.Text component to update.
 * @param color The new color to apply to the text.
 */
fun MementoController.updateText(id: Int, color: Color) {
    update<MementoState.Text>(id) {
        it.updateTextColor(color.value)
    }
}

/**
 * Updates the rotation of a MementoState with the given ID by a specified delta.
 *
 * This function modifies the rotation of the MementoState identified by `id`.
 * The new rotation is calculated by adding `rotationDelta` to the current rotation.
 *
 * @param id The unique identifier of the MementoState to update.
 * @param rotationDelta The amount to change the rotation by. Positive values rotate clockwise,
 * negative values rotate counter-clockwise.
 * @return The new, updated rotation value of the MementoState.
 */
fun MementoController.updateRotation(id: Int, rotationDelta: Float): Float {
    var updatedRotation = 0f

    update<MementoState>(id) {
        updatedRotation = it.rotation + rotationDelta
        it.updateRotation(updatedRotation)
    }
    return updatedRotation
}

fun MementoController.updateScale(id: Int, scale: Float) {
    update<MementoState>(id) {
        it.updateScale(it.scale * scale)
    }
}

/**
 * Updates the layout of a Memento component by applying a drag amount.
 *
 * This function modifies the offsetX and offsetY properties of the MementoState
 * identified by the given `id` by adding the x and y components of the `dragAmount` Offset.
 * After updating the layout, it brings the component to the front of the display order.
 *
 * @param id The unique identifier of the Memento component to update.
 * @param dragAmount The Offset representing the amount to drag the component.
 *                   The x component will be added to offsetX and the y component to offsetY.
 */
fun MementoController.updateLayout(id: Int, dragAmount: Offset) {
    update<MementoState>(id) {
        it.updateLayout(
            offsetX = it.offsetX + dragAmount.x,
            offsetY = it.offsetY + dragAmount.y
        )
    }
    bringToFront(id)
}

/**
 * Creates a new text component and adds it to the Memento state.
 *
 * @param offset The initial offset of the text component.
 * @param initialText The initial text content.
 * @param seedColor The initial color of the text.
 */
fun MementoController.createText(
    offset: Offset,
    initialText: String,
    seedColor: Color
) {
    val component = MementoState.Text(
        id = state.value.size + 1,
        text = initialText,
        offsetX = offset.x,
        offsetY = offset.y,
        scale = 1f,
        rotation = 0f,
        seedColor = seedColor.value
    )

    _state.update { it + component }
}

/**
 * Attaches an image to the Memento.
 *
 * This function creates a new `MementoState.Image` with the provided content and adds it to the
 * current list of memento states. The image is initially placed at the origin (0,0) with no
 * rotation and a scale of 1.
 *
 * @param content A composable function that defines the content of the image.
 */
fun MementoController.attachImage(
    content: @Composable () -> Unit,
) {
    val component = MementoState.Image(
        id = state.value.size + 1,
        offsetX = 0f,
        offsetY = 0f,
        scale = 1f,
        rotation = 0f,
        content = content,
    )

    _state.update { it + component }
}
