package com.takealook.memento.sticker

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable

class MementoStickerBuilder {
    internal val stickers = mutableListOf<@Composable LazyGridItemScope.() -> Unit>()

    internal fun build(): List<@Composable LazyGridItemScope.() -> Unit> = stickers
}

fun MementoStickerBuilder.sticker(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    stickers.add(content)
}
