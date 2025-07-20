package my.takealook.memento

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

/**
 * A [Modifier] that enables gesture interactions for a memento item.
 *
 * This modifier handles:
 * - Bringing the memento to the front when tapped.
 * - Animating the memento's offset when its position changes.
 * - Applying rotation to the memento.
 *
 * @param state The current [MementoState] of the memento item. If null, the modifier does nothing.
 * @param holder The [MementoStateHolder] that manages the state of all memento items.
 * @return A [Modifier] with gesture handling capabilities.
 */
internal fun Modifier.mementoGesture(
    state: MementoState?,
    holder: MementoController
) = composed {
    if (state == null) return@composed this

    val animatedOffset = remember {
        Animatable(
            Offset(
                state.offsetX,
                state.offsetY
            ), Offset.VectorConverter
        )
    }

    val isFocused by holder.isTextFocused.collectAsStateWithLifecycle()

    // TODO: Implement Rotate, Scale Animation here

    LaunchedEffect(isFocused) {
        animatedOffset.animateTo(
            targetValue = Offset(state.offsetX, state.offsetY),
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(state.offsetX, state.offsetY) {
        if (!isFocused && animatedOffset.targetValue != Offset(state.offsetX, state.offsetY)) {
            animatedOffset.snapTo(Offset(state.offsetX, state.offsetY))
        }
    }

    this
        .offset {
            IntOffset(
                animatedOffset.value.x.roundToInt(),
                animatedOffset.value.y.roundToInt()
            )
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    holder.bringToFront(state.id)
                }
            )
        }.graphicsLayer {
            rotationZ = state.rotation
            transformOrigin = TransformOrigin.Center
        }
}
