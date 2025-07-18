package com.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MementoStateHolder {
    internal val _state: MutableStateFlow<List<MementoState>> = MutableStateFlow(emptyList())
    val state = _state.asStateFlow()

    internal var focusId: Int? = null
    internal var savedOffset: Offset? = null
    internal var savedRotation: Float? = null
    internal var savedScale: Float? = null

    private val _isTextFocused = MutableStateFlow(false)
    val isTextFocused = _isTextFocused.asStateFlow()

    private val _isCaptureRequested = MutableStateFlow(false)
    internal val isCaptureRequested = _isCaptureRequested.asStateFlow()

    fun executeTextFocus(
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
                focusId = id
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

    fun requestFocusMode(enabled: Boolean = true) {
        _isTextFocused.value = enabled
    }

    fun releaseFocus() {
        if (savedOffset == null) return
        val components = state.value.toMutableList()
        val updatedComponents = components.map {
            if (it.id == focusId!!) {
                it.updateLayout(
                    offsetX = savedOffset!!.x,
                    offsetY = savedOffset!!.y
                ).updateRotation(savedRotation!!).updateScale(savedScale!!)
            } else it
        }
        savedOffset = null
        savedRotation = null
        savedScale = null
        focusId = null
        components.clear()
        components.addAll(updatedComponents)
        _state.value = components
        _isTextFocused.value = false
    }

    fun bringToFront(id: Int) {
        val components = state.value.toMutableList()
        val index = components.indexOfFirst { it.id == id }
        if (index != -1) {
            val component = components.removeAt(index)
            components.add(component)
            _state.value = components
        }
    }

    fun requestCapture() {
        _isCaptureRequested.value = true
    }

    internal fun finishCapture() {
        _isCaptureRequested.value = false
    }

    companion object {
        private val TYPE = "type"
        private val ID = "id"
        private val OFFSET_X = "offsetX"
        private val OFFSET_Y = "offsetY"
        private val SCALE = "scale"
        private val ROTATION = "rotation"
        private val IMAGE_BYTES = "key"
        private val CONTENT_DESCRIPTION = "contentDescription"
        private val TEXT = "text"
        private val COLOR = "color"

        val Saver = listSaver<MementoStateHolder, Map<String, Any?>>(
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
                MementoStateHolder().apply { _state.value = mappedState }
            }
        )
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : MementoState> MementoStateHolder.update(id: Int, onUpdate: (T) -> MementoState) {
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

fun MementoStateHolder.updateText(id: Int, color: Color) {
    update<MementoState.Text>(id) {
        it.updateTextColor(color.value)
    }
}

fun MementoStateHolder.updateRotation(id: Int, rotationDelta: Float): Float {
    var updatedRotation = 0f

    update<MementoState>(id) {
        updatedRotation = it.rotation + rotationDelta
        it.updateRotation(updatedRotation)
    }
    return updatedRotation
}

fun MementoStateHolder.updateScale(id: Int, scale: Float) {
    update<MementoState>(id) {
        it.updateScale(it.scale * scale)
    }
}

fun MementoStateHolder.updateLayout(id: Int, dragAmount: Offset) {
    update<MementoState>(id) {
        it.updateLayout(
            offsetX = it.offsetX + dragAmount.x,
            offsetY = it.offsetY + dragAmount.y
        )
    }
    bringToFront(id)
}

internal fun MementoStateHolder.createText(
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

fun MementoStateHolder.attachImage(
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

@Composable
fun rememberMementoStateHolder(): MementoStateHolder {
    return rememberSaveable(saver = MementoStateHolder.Saver) { MementoStateHolder() }
}
