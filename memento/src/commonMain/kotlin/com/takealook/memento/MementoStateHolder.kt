package com.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MementoStateHolder {
    internal val _state: MutableStateFlow<List<MementoState>> = MutableStateFlow(emptyList())
    val state = _state.asStateFlow()
}

fun MementoStateHolder.updateLayout(id: Int, offset: Offset) {
    val components = state.value.toMutableList()
    val updatedComponents = components.map {
        if (it.id == id) {
            it.updateLayout(offsetX = offset.x, offsetY = offset.y)
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
        offsetY = offset.y
    )

    _state.update { it + component }
}

fun MementoStateHolder.createImage(offset: Offset) {
    // TODO : Must Implement Image
    val component = MementoState.Image(
        id = state.value.size + 1,
        offsetX = 0f,
        offsetY = 0f
    )

    _state.update { it + component }
}

fun MementoStateHolder.createSticker(offset: Offset) {
    // TODO : Must Implement Sticker
    val component = MementoState.Sticker(
        id = state.value.size + 1,
        offsetX = 0f,
        offsetY = 0f
    )

    _state.update { it + component }
}

@Composable
fun rememberMementoStateHolder(): MementoStateHolder {
    // TODO : Must Implement rememberSaveable
    return remember { MementoStateHolder() }
}
