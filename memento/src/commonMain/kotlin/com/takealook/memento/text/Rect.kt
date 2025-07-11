package com.takealook.memento.text

import androidx.compose.ui.geometry.Rect

fun Rect.addHorizontalPadding(padding: Float): Rect {
    return this.copy(
        left = left - padding, top,
        right = right + padding, bottom
    )
}
