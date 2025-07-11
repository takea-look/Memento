package com.takealook.memento.text

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

interface TextShapePadding {

    fun calculatePadding(density: Density, textStyle: TextStyle): Float

    data class Fixed(private val padding: Dp) : TextShapePadding {
        override fun calculatePadding(density: Density, textStyle: TextStyle): Float = with(density) {
            return padding.toPx()
        }
    }

    data object Flexible : TextShapePadding {
        override fun calculatePadding(density: Density, textStyle: TextStyle): Float = with(density) {
            return textStyle.lineHeight.toPx() - textStyle.fontSize.toPx()
        }
    }
}