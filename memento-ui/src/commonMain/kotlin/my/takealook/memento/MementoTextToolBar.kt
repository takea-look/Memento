package my.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
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

/**
 * Generates a dynamic color scheme based on a seed color for the Memento components.
 *
 * This function utilizes the `dynamicColorScheme` from the `materialkolor` library
 * to create a light theme color scheme. It employs the `Expressive` palette style
 * and adheres to the `SPEC_2025` color specification.
 *
 * @param seedColor The base color from which the dynamic color scheme will be generated.
 * @return A [com.materialkolor.scheme.ColorScheme] object representing the generated color scheme.
 */
fun getMementoColorScheme(seedColor: Color) = dynamicColorScheme(
    seedColor = seedColor,
    isDark = false,
    style = PaletteStyle.Expressive,
    specVersion = ColorSpec.SpecVersion.SPEC_2025
)

@Composable
fun getMementoColorScheme(colors: MementoState.Text.Colors) = MaterialTheme.colorScheme.copy(
    primary = Color(colors.contentColor),
    primaryContainer = Color(colors.containerColor)
)

/**
 * A Composable that displays a horizontal row of color items representing a rainbow palette.
 *
 * This palette includes a predefined set of rainbow colors (Red, Orange, Yellow, Green, Blue, Indigo, Violet).
 * Each color is displayed as a [MementoPaletteItem].
 *
 * @param modifier The modifier to be applied to the LazyRow containing the palette items.
 * @param onColorClick A lambda function that will be invoked with the selected [Color] when a palette item is clicked.
 */
@Composable
fun MementoRainbowPalette(
    modifier: Modifier = Modifier,
    onColorClick: (colors: MementoState.Text.Colors) -> Unit = {}
) {
    val solidColors = listOf(
        White to Black,
        Black to White
    )

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
        items(solidColors) { (container, content) ->
            val scheme = MaterialTheme.colorScheme.copy(
                primary = content,
                primaryContainer = container,
            )
            MementoPaletteItem(
                colorScheme = scheme,
                onColorClick = {
                    onColorClick(
                        MementoState.Text.Colors(
                            containerColor = container.value,
                            contentColor = content.value
                        )
                    )
                }
            )
        }

        items(colors) { seedColor ->
            val scheme = getMementoColorScheme(seedColor)
            val colors = MementoState.Text.Colors(
                containerColor = scheme.primaryContainer.value,
                contentColor = scheme.primary.value
            )
            MementoPaletteItem(
                seedColor = seedColor,
                onColorClick = { onColorClick(colors) }
            )
        }
    }
}

/**
 * A Composable that displays a single color item in the Memento palette.
 *
 * This item consists of a circular shape with a primary color fill and a primary container color border.
 * It is clickable, and the provided [onColorClick] lambda will be invoked when it's clicked.
 *
 * @param modifier The modifier to be applied to the palette item.
 * @param seedColor The base color from which the primary and primary container colors will be derived.
 * @param onColorClick A lambda function to be invoked when the palette item is clicked.
 */
@Composable
fun MementoPaletteItem(
    modifier: Modifier = Modifier,
    seedColor: Color,
    onColorClick: () -> Unit = {}
) {
    val colorScheme = getMementoColorScheme(seedColor)

    MementoPaletteItem(
        modifier = modifier,
        colorScheme = colorScheme,
        onColorClick = onColorClick
    )
}

@Composable
fun MementoPaletteItem(
    modifier: Modifier = Modifier,
    colorScheme: ColorScheme,
    onColorClick: () -> Unit = {}
) {
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
