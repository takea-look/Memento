package my.takealook.memento.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextLayoutResult

/**
 * Returns the bounding box of the line.
 *
 * @param lineIndex the line number
 * @return the bounding box of the line.
 */
fun TextLayoutResult.getLineRect(lineIndex: Int): Rect {
    return Rect(
        left = getLineLeft(lineIndex),
        top = getLineTop(lineIndex),
        right = getLineRight(lineIndex),
        bottom = getLineBottom(lineIndex))
}
