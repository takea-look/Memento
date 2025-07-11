package com.takealook.memento.text

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

interface TextShapeCorners {

    fun calculateRadius(density: Density, textStyle: TextStyle): Float

    data class Fixed(private val radius: Dp) : TextShapeCorners {
        override fun calculateRadius(density: Density, textStyle: TextStyle): Float = with(density) {
            return radius.toPx()
        }
    }

    data class Flexible(
        private val fraction: Float = 0.45f
    ) : TextShapeCorners {
        override fun calculateRadius(density: Density, textStyle: TextStyle): Float = with(density) {
            return textStyle.lineHeight.toPx() * fraction
        }
    }
}