package com.takealook.memento.sticker

import androidx.compose.runtime.key
import com.takealook.memento.Cacheable

data class MementoSticker<T>(
    private val key: String,
    val image: T,
    val contentDescription: String? = null
) {
    val cacheKey: Key = Key(key)
}

data class Key(
    override val key: String
) : Cacheable {
    override fun toString(): String {
        return key
    }
}
