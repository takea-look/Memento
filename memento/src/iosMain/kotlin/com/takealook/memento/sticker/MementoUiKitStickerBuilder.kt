package com.takealook.memento.sticker

import platform.UIKit.UIView

class MementoUiKitStickerBuilder {
    internal val stickers = mutableListOf<() -> UIView>()

    internal fun build(): List<() -> UIView> = stickers

    fun sticker(
        content: () -> UIView
    ) {
        stickers.add(content)
    }
}

