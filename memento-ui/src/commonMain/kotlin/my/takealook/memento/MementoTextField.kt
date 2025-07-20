package my.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import my.takealook.memento.text.TextShape

internal val baseFontSize = 24.sp

/**
 * A Composable function that displays a text field with Memento styling.
 * This version of MementoTextField takes a [MementoState.Text] object to manage its state.
 *
 * @param state The [MementoState.Text] object that holds the current state of the text field,
 *              including text content, scale, and seed color.
 * @param modifier Optional [Modifier] to be applied to this Composable.
 * @param textStyle Optional [TextStyle] to customize the appearance of the text.
 *                  The font size will be scaled based on `state.scale` and `baseFontSize`.
 *                  The text color and background color will be derived from `state.seedColor`.
 * @param onFocused A lambda function to be invoked when the text field gains focus.
 * @param onKeyDown A lambda function to be invoked when a key is pressed while the text field is focused.
 */
@Composable
fun MementoTextField(
    state: MementoState.Text,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onFocused: () -> Unit = {},
    onKeyDown: () -> Unit = {},
) {
    val scaledFontSize = (baseFontSize.value * state.scale).sp
    val colorScheme = remember(state.seedColor) {
        getMementoColorScheme(androidx.compose.ui.graphics.Color(state.seedColor))
    }
    val textStyle = textStyle.copy(
        fontSize = scaledFontSize,
        color = colorScheme.onPrimaryContainer,
        background = colorScheme.primaryContainer
    )
    val textState = rememberTextFieldState(state.text)

    MementoTextField(
        state = textState,
        textStyle = textStyle,
        onFocused = onFocused,
        onKeyDown = onKeyDown,
        modifier = modifier
    )
}

/**
 * A composable function that displays a text field with Memento-specific styling and behavior.
 *
 * This version of `MementoTextField` takes a [TextFieldState] directly, offering more control
 * over the text field's internal state.
 *
 * @param state The [TextFieldState] managing the text content and selection.
 * @param modifier The [Modifier] to be applied to this text field.
 * @param textStyle The [TextStyle] to be applied to the text. Defaults to [TextStyle.Default].
 * @param onFocused A callback invoked when the text field gains focus.
 * @param onKeyDown A callback invoked when a key is pressed while the text field is focused.
 */
@Composable
fun MementoTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onFocused: () -> Unit = {},
    onKeyDown: () -> Unit = {},
) {
    val mainFontSize = textStyle
        .fontSize
        .takeIf(TextUnit::isSpecified)
        ?: baseFontSize

    val interactionSource = remember { MutableInteractionSource() }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val textShape by remember {
        derivedStateOf { textLayoutResult?.let { TextShape(it) } }
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Press) {
                onKeyDown()
            }
        }
    }

    Box(modifier = modifier) {
        BasicTextField(
            state = state,
            textStyle = textStyle.copy(
                background = TextStyle.Default.background,
                fontSize = mainFontSize,
                lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None),
                lineHeight = mainFontSize * 1.3f,
                textAlign = TextAlign.Center
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .then(
                    other = textShape?.let {
                        Modifier.background(textStyle.background, it)
                    } ?: Modifier
                )
                .preventAutoWrap()
                .width(IntrinsicSize.Min)
                .defaultMinSize(minWidth = 10.dp)
                .onFocusChanged {
                    if (it.isFocused) {
                        onFocused()
                    }
                },
            onTextLayout = { result ->
                textLayoutResult = result()
            },
            cursorBrush = SolidColor(textStyle.color)
        )
    }
}

/**
 * Prevents the text field from automatically wrapping to the next line when the text exceeds the
 * available width. Instead, it allows the text field to grow horizontally to accommodate the
 * entire text content. This is achieved by measuring the text with an infinite maximum width
 * constraint, and then laying out the text field with its measured width.
 *
 * This is useful in scenarios where you want the text field to expand dynamically based on its
 * content, rather than being constrained by its parent's width. For example, when implementing
 * a tag input field or a text editor where lines should only wrap explicitly with newline
 * characters.
 */
fun Modifier.preventAutoWrap(): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = Constraints.Infinity
        )
    )

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}