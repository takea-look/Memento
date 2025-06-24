package com.takealook.memento

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

/**
 * calculate the actual offset of a rotated components.
 * It uses Rotation Matrix.
 */
fun getRotatedPan(pan: Offset, rotationAmount: Float): Offset {
    val radians = toRadians(rotationAmount)
    val sin = sin(radians)
    val cos = cos(radians)

    val adjustedPan = Offset(
        (pan.x * cos - pan.y * sin).toFloat(),
        (pan.x * sin + pan.y * cos).toFloat()
    )
    return adjustedPan
}

fun toRadians(degrees: Float): Float = (degrees * kotlin.math.PI / 180f).toFloat()
