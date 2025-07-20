package my.takealook.memento.text

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

/**
 * Defines how the corners of a text shape are rounded.
 * This interface allows for different strategies for calculating the corner radius,
 * such as using a fixed Dp value or a fraction of the text's line height.
 */
interface TextShapeCorners {

    /**
     * Calculates the radius for the text shape corners.
     *
     * @param density The current [Density] to convert Dp to Px.
     * @param textStyle The [TextStyle] of the text to potentially base the radius on.
     * @return The calculated radius in pixels.
     */
    fun calculateRadius(density: Density, textStyle: TextStyle): Float

    /**
     * Represents a fixed corner radius for a text shape.
     *
     * @property radius The fixed radius value in Dp.
     */
    data class Fixed(private val radius: Dp) : TextShapeCorners {
        override fun calculateRadius(density: Density, textStyle: TextStyle): Float = with(density) {
            return radius.toPx()
        }
    }

    /**
     * A [TextShapeCorners] implementation that calculates the radius based on a fraction of the text's line height.
     *
     * This allows the corner radius to adapt to different font sizes and line heights, maintaining a consistent
     * visual appearance.
     *
     * @property fraction The fraction of the text's line height to use for the radius.
     *                    Defaults to `0.45f`, which typically results in a rounded, pill-like shape.
     */
    data class Flexible(
        private val fraction: Float = 0.45f
    ) : TextShapeCorners {
        override fun calculateRadius(density: Density, textStyle: TextStyle): Float = with(density) {
            return textStyle.lineHeight.toPx() * fraction
        }
    }
}