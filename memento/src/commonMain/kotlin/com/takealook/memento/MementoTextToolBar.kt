package com.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec
import org.jetbrains.compose.ui.tooling.preview.Preview

private val Red     = Color(0xFFFF0000)
private val Orange  = Color(0xFFFFA500)
private val Yellow  = Color(0xFFFFFF00)
private val Green   = Color(0xFF00FF00)
private val Blue    = Color(0xFF0000FF)
private val Indigo  = Color(0xFF4B0082)
private val Violet  = Color(0xFF8B00FF)

fun getMementoColorScheme(seedColor: Color) = dynamicColorScheme(
    seedColor = seedColor,
    isDark = false,
    style = PaletteStyle.Expressive,
    specVersion = ColorSpec.SpecVersion.SPEC_2025
)

@Composable
fun MementoRainbowPalette(
    modifier: Modifier = Modifier,
    onColorClick: (Color) -> Unit = {}
) {
    val colors = listOf(
        Red,
        Orange,
        Yellow,
        Green,
        Blue,
        Indigo,
        Violet
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(colors) { seedColor ->
            MementoPaletteItem(
                seedColor = seedColor,
                onColorClick = { onColorClick(seedColor) }
            )
        }
    }
}

@Composable
fun MementoPaletteItem(
    modifier: Modifier = Modifier,
    seedColor: Color,
    onColorClick: () -> Unit = {}
) {
    val colorScheme = getMementoColorScheme(seedColor)

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
            .clip(CircleShape)
            .clickable(onClick = onColorClick)
            .background(colorScheme.primaryContainer)
            .padding(2.dp)
            .clip(CircleShape)
            .background(colorScheme.primary)
    )
}

@Preview
@Composable
fun MementoPalettePreview() {
    MementoRainbowPalette()
}
