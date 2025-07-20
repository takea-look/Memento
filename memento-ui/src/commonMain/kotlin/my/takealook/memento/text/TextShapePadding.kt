package my.takealook.memento.text

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

/**
 * Defines a strategy for calculating padding around text within a shape.
 * This allows for different approaches to padding, such as fixed values or values
 * that adapt to the text's style.
 */
interface TextShapePadding {

    /**
     * Calculates the padding value based on the given [density] and [textStyle].
     *
     * @param density The [Density] to use for converting Dp to Px.
     * @param textStyle The [TextStyle] to use for calculating padding, particularly relevant for [Flexible] padding.
     * @return The calculated padding value in pixels (Px).
     */
    fun calculatePadding(density: Density, textStyle: TextStyle): Float

    /**
     * A [TextShapePadding] that uses a fixed [Dp] value for padding.
     *
     * @param padding The fixed padding value to use.
     */
    data class Fixed(private val padding: Dp) : TextShapePadding {
        override fun calculatePadding(density: Density, textStyle: TextStyle): Float = with(density) {
            return padding.toPx()
        }
    }

    /**
     * A [TextShapePadding] that calculates padding based on the difference between the line height
     * and font size of the provided [TextStyle]. This allows the padding to adapt to changes
     * in the text's appearance.
     */
    data object Flexible : TextShapePadding {
        override fun calculatePadding(density: Density, textStyle: TextStyle): Float = with(density) {
            return textStyle.lineHeight.toPx() - textStyle.fontSize.toPx()
        }
    }
}