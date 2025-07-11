package com.takealook.memento.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextLayoutResult

fun TextLayoutResult.getLineRect(lineIndex: Int): Rect {
    return Rect(
        left = getLineLeft(lineIndex),
        top = getLineTop(lineIndex),
        right = getLineRight(lineIndex),
        bottom = getLineBottom(lineIndex))
}
