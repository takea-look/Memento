package my.takealook.memento.text

import androidx.compose.ui.geometry.Rect

/**
 * Adds horizontal padding to the [Rect].
 *
 * @param padding The amount of padding to add to the left and right sides.
 * @return A new [Rect] with the horizontal padding applied.
 */
fun Rect.addHorizontalPadding(padding: Float): Rect {
    return this.copy(
        left = left - padding, top,
        right = right + padding, bottom
    )
}
