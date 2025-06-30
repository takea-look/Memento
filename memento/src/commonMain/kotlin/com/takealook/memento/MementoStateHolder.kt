package com.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MementoStateHolder {
    internal val _state: MutableStateFlow<List<MementoState>> = MutableStateFlow(emptyList())
    val state = _state.asStateFlow()

    private val _isStickerSheetOpened = MutableStateFlow(false)
    val isStickerSheetOpened = _isStickerSheetOpened.asStateFlow()

    fun openStickerSheet() {
        _isStickerSheetOpened.value = true
    }

    fun closeStickerSheet() {
        _isStickerSheetOpened.value = false
    }

    companion object {
        val Saver = listSaver<MementoStateHolder, MementoState>(
            save = { it.state.value },
            restore = { MementoStateHolder().apply { _state.value = it } }
        )
    }
}

fun MementoStateHolder.updateRotation(id: Int, rotationDelta: Float): Float {
    var updatedRotation = 0f
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            updatedRotation = it.rotation + rotationDelta
            it.updateRotation(updatedRotation)
        } else it
        }
    components.clear()
    components.addAll(updatedComponents)
    _state.value = components

    return updatedRotation
}

fun MementoStateHolder.updateScale(id: Int, scale: Float) {
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            it.updateScale(it.scale * scale)
        } else it
    }
    components.clear()
    components.addAll(updatedComponents)
    _state.value = components
}

fun MementoStateHolder.updateLayout(id: Int, dragAmount: Offset) {
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            it.updateLayout(
                offsetX = it.offsetX + dragAmount.x,
                offsetY = it.offsetY + dragAmount.y
            )
        } else it
    }

    components.clear()
    components.addAll(updatedComponents)
    _state.value = components
}

fun MementoStateHolder.createText(
    offset: Offset,
    initialText: String = "Text"
) {
    val component = MementoState.Text(
        id = state.value.size + 1,
        text = initialText,
        offsetX = offset.x,
        offsetY = offset.y,
        scale = 1f,
        rotation = 0f
    )

    _state.update { it + component }
}

fun MementoStateHolder.createImage(
    offset: Offset,
    imageCacheKey: String,
    contentDescription: String? = null
) {
    // TODO : Must Implement Image
    val component = MementoState.Image(
        id = state.value.size + 1,
        offsetX = offset.x,
        offsetY = offset.y,
        scale = 1f,
        rotation = 0f,
        key = imageCacheKey,
        contentDescription = contentDescription
    )

    _state.update { it + component }
}

@Composable
fun rememberMementoStateHolder(): MementoStateHolder {
    return rememberSaveable(saver = MementoStateHolder.Saver) { MementoStateHolder() }
}
