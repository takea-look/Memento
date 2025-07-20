package my.takealook.memento.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/**
 * A [Shape] that draws a rounded rectangle around the text.
 *
 * This shape is useful for creating chat bubbles or other UI elements that need to wrap around text.
 *
 * @param textLayoutResult The [TextLayoutResult] of the text to wrap.
 * @param padding The padding to apply to the shape.
 * @param corners The corners to apply to the shape.
 */
class TextShape(
    private val textLayoutResult: TextLayoutResult,
    private val padding: TextShapePadding = TextShapePadding.Flexible,
    private val corners: TextShapeCorners = TextShapeCorners.Fixed(12.dp),
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val lineCount = textLayoutResult.lineCount
        val textStyle = textLayoutResult.layoutInput.style
        val lineHeight = with(density) {
            textStyle.lineHeight.toPx()
        }

        val lineRects = mutableMapOf<Int, Rect>()
        val paddingPx = padding.calculatePadding(density, textStyle)
        val curveRadiusPx = corners.calculateRadius(density, textStyle).coerceIn(0f, lineHeight / 2)

        val shapePath = Path().apply {
            // left-top -> right-top
            var previousLine: Rect = lineRects.getOrPut(0) {
                textLayoutResult.getLineRect(0).addHorizontalPadding(paddingPx)
            }
            moveTo(previousLine.left, previousLine.top + curveRadiusPx)
            quadraticBezierTo(
                x1 = previousLine.left, y1 = previousLine.top,
                x2 = previousLine.left + curveRadiusPx, y2 = previousLine.top
            )
            lineTo(previousLine.right - curveRadiusPx, previousLine.top)
            quadraticBezierTo(
                x1 = previousLine.right, y1 = previousLine.top,
                x2 = previousLine.right, y2 = previousLine.top + curveRadiusPx
            )
            lineTo(previousLine.right, previousLine.bottom - curveRadiusPx)

            for (i in 1 until lineCount) {
                val currentLine = lineRects.getOrPut(i) {
                    textLayoutResult.getLineRect(i).addHorizontalPadding(paddingPx)
                }

                when {
                    abs(currentLine.right - previousLine.right) > curveRadiusPx -> {
                        val normalizedCurveRadius = if (currentLine.right > previousLine.right) curveRadiusPx else -curveRadiusPx
                        quadraticBezierTo(
                            x1 = previousLine.right, y1 = previousLine.bottom,
                            x2 = previousLine.right + normalizedCurveRadius, y2 = currentLine.top
                        )
                        lineTo(currentLine.right - normalizedCurveRadius, currentLine.top)
                        quadraticBezierTo(
                            x1 = currentLine.right, y1 = currentLine.top,
                            x2 = currentLine.right, y2 = currentLine.top + curveRadiusPx
                        )
                    }
                    else -> {
                        cubicTo(
                            x1 = previousLine.right, y1 = previousLine.bottom,
                            x2 = currentLine.right, y2 = currentLine.top,
                            x3 = currentLine.right, y3 = currentLine.top + curveRadiusPx
                        )
                    }
                }
                lineTo(currentLine.right, currentLine.bottom - curveRadiusPx)
                previousLine = currentLine
            }

            quadraticBezierTo(
                x1 = previousLine.right, y1 = previousLine.bottom,
                x2 = previousLine.right - curveRadiusPx, y2 = previousLine.bottom
            )
            lineTo(previousLine.left + curveRadiusPx, previousLine.bottom)
            quadraticBezierTo(
                x1 = previousLine.left, y1 = previousLine.bottom,
                x2 = previousLine.left, y2 = previousLine.bottom - curveRadiusPx
            )
            lineTo(previousLine.left, previousLine.top + curveRadiusPx)

            for (i in lineCount - 2 downTo 0) {
                val currentLine = lineRects.getOrPut(i) {
                    textLayoutResult.getLineRect(i).addHorizontalPadding(paddingPx)
                }

                when {
                    abs(previousLine.left - currentLine.left) > curveRadiusPx -> {
                        val normalizedCurveRadius = if (previousLine.left > currentLine.left) -curveRadiusPx else curveRadiusPx
                        quadraticBezierTo(
                            x1 = previousLine.left, y1 = previousLine.top,
                            x2 = previousLine.left + normalizedCurveRadius, y2 = currentLine.bottom
                        )
                        lineTo(currentLine.left - normalizedCurveRadius, currentLine.bottom)
                        quadraticBezierTo(
                            x1 = currentLine.left, y1 = currentLine.bottom,
                            x2 = currentLine.left, y2 = currentLine.bottom - curveRadiusPx
                        )
                    }
                    else -> {
                        cubicTo(
                            x1 = previousLine.left, y1 = previousLine.top,
                            x2 = currentLine.left, y2 = currentLine.bottom,
                            x3 = currentLine.left, y3 = currentLine.bottom - curveRadiusPx
                        )
                    }
                }
                lineTo(currentLine.left, currentLine.top + curveRadiusPx)
                previousLine = currentLine
            }
            // Close the path
            close()

        }

        return Outline.Generic(shapePath)
    }
}