package com.takealook.memento.sticker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.window.ComposeUIViewController
import com.takealook.memento.MementoEditor

@OptIn(ExperimentalMaterial3Api::class)
fun MementoEditorViewController(
    stickerBuilder: MementoUiKitStickerBuilder.() -> Unit = {}
) = ComposeUIViewController {
    val stickers = remember {
        MementoUiKitStickerBuilder()
            .apply(stickerBuilder)
            .build()
    }

    MementoEditor {
        stickers.forEach {
            sticker {
                // FIXME : UIKitView won't represented on the ModalBottomSheet Composable.
                UIKitView(
                    factory = { it.invoke() },
                )
            }
        }
    }
}
