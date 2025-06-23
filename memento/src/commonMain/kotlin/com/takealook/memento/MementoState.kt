package com.takealook.memento

interface Layout {
    val offsetX: Float
    val offsetY: Float

    fun updateLayout(offsetX: Float, offsetY: Float): MementoState
}

interface Identifiable {
    val id: Int
}

sealed interface MementoState : Layout, Identifiable {
    data class Image(
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float
    ) : MementoState {
        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
            return copy(offsetX = offsetX, offsetY = offsetY)
        }
    }

    data class Text(
        val text: String,
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float
    ) : MementoState {
        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
            return copy(offsetX = offsetX, offsetY = offsetY)
        }

        fun updateText(text: String): MementoState {
            return copy(text = text)
        }
    }

    data class Sticker(
        override val id: Int,
        override val offsetX: Float,
        override val offsetY: Float
    ) : MementoState {
        override fun updateLayout(offsetX: Float, offsetY: Float): MementoState {
            return copy(offsetX = offsetX, offsetY = offsetY)
        }
    }
}
