package com.takealook.memento.sticker

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable

class MementoStickerBuilder<T> {
    internal val stickers = mutableListOf<T>()

    internal fun build(): List<T> = stickers
}

fun <T> MementoStickerBuilder<T>.sticker(
    content: T
) {
    stickers.add(content)
}
