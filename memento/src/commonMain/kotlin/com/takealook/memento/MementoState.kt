package com.takealook.memento

interface Layout {
    val offsetX: Float
    val offsetY: Float
    val scale: Float
    val rotation: Float

    fun updateLayout(offsetX: Float, offsetY: Float): MementoState
    fun updateScale(scale: Float): MementoState
    fun updateRotation(rotation: Float): MementoState
}

interface Identifiable {
    val id: Int
}

sealed interface MementoState : Layout, Identifiable {
    data class Image(
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float,
        override val rotation: Float
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

    data class Text(
        val text: String,
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float,
        override val rotation: Float
    ) : MementoState {
        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
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

    data class Sticker(
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float,
        override val rotation: Float
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
}
