package my.takealook.memento

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Creates and remembers a [MementoController] instance.
 *
 * This function uses [rememberSaveable] to ensure that the [MementoController] state is preserved
 * across configuration changes (e.g., screen rotations) and process death.
 *
 * @return A remembered [MementoController] instance.
 */
@Composable
fun rememberMementoController(): MementoController {
    return rememberSaveable(saver = MementoController.Saver) { MementoController() }
}
