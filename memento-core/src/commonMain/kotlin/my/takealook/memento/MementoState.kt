package my.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * Interface representing the layout properties of an element.
 * This includes its position (offset X and Y), scale, and rotation.
 * It also defines methods to update these properties and return a new MementoState.
 */
interface Layout {
    /**
     * The horizontal offset of the layout.
     */
    val offsetX: Float
    /**
     * The vertical offset of the layout.
     */
    val offsetY: Float
    /**
     * The scale of the memento components.
     */
    val scale: Float

    /**
     * The rotation of the memento in degrees.
     */
    val rotation: Float

    /**
     * Updates the layout of the memento state.
     *
     * @param offsetX The new X offset.
     * @param offsetY The new Y offset.
     * @return A new MementoState with the updated layout.
     */
    fun updateLayout(offsetX: Float, offsetY: Float): MementoState

    /**
     * Updates the scale of the memento state.
     *
     * @param scale The new scale value.
     * @return A new MementoState instance with the updated scale.
     */
    fun updateScale(scale: Float): MementoState

    /**
     * Updates the rotation of the memento.
     *
     * @param rotation The new rotation value.
     * @return A new MementoState instance with the updated rotation.
     */
    fun updateRotation(rotation: Float): MementoState
}

/**
 * Interface for objects that have an identifier.
 */
interface Identifiable {
    val id: Int
}

/**
 * Represents the state of a memento, which can be either an image or text.
 * It combines layout properties (offset, scale, rotation) from the [Layout] interface
 * and an identifier from the [Identifiable] interface.
 *
 * This sealed interface is used to define the different types of content that can be
 * part of a memento.
 */
sealed interface MementoState : Layout, Identifiable {

    /**
     * Represents an image memento state.
     *
     * @property id The unique identifier of the image.
     * @property offsetX The horizontal offset of the image.
     * @property offsetY The vertical offset of the image.
     * @property scale The scale factor of the image.
     * @property rotation The rotation angle of the image in degrees.
     * @property content A composable function that renders the image content.
     */
    @Serializable
    data class Image(
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float,
        override val rotation: Float,
        val content: @Composable () -> Unit
    ) : MementoState {
        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
            return copy(offsetX = offsetX, offsetY = offsetY)
        }

        override fun updateScale(scale: Float): MementoState {
            return copy(scale = scale)
        }

        override fun updateRotation(rotation: Float): MementoState {
            return copy(rotation = rotation)
        }
    }

    /**
     * Represents a text element in the memento state.
     *
     * @property text The actual text content.
     * @property id The unique identifier for this text element.
     * @property offsetX The horizontal offset of the text element.
     * @property offsetY The vertical offset of the text element.
     * @property scale The scaling factor of the text element.
     * @property rotation The rotation angle of the text element.
     * @property seedColor The color of the text, represented as a ULong.
     */
    @Serializable
    data class Text(
        val text: String,
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float,
        override val rotation: Float,
        val seedColor: ULong,
    ) : MementoState {

        fun updateTextColor(seedColor: ULong): MementoState {
            return copy(seedColor = seedColor)
        }

        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
            Color(Color.Blue.value)
            return copy(offsetX = offsetX, offsetY = offsetY)
        }

        fun updateText(text: String): MementoState {
            return copy(text = text)
        }

        override fun updateScale(scale: Float): MementoState {
            return copy(scale = scale)
        }

        override fun updateRotation(rotation: Float): MementoState {
            return copy(rotation = rotation)
        }
    }
}
