package com.takealook.memento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.takealook.memento.sticker.MementoSticker
import com.takealook.memento.sticker.MementoStickerBuilder
import com.takealook.memento.sticker.sticker

@Composable
fun MementoEditor(
    builder: MementoStickerBuilder<MementoSticker<Any>>.() -> Unit
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(StickerKeyFetcher.Factory())
            }
            .logger(DebugLogger())
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .crossfade(true)
            .build()
    }
    val holder = rememberMementoStateHolder()

    val stickers = remember {
        MementoStickerBuilder<MementoSticker<Any>>()
            .apply(builder)
            .build()
    }

    MementoEditor(
        stateHolder = holder,
        modifier = Modifier.fillMaxSize()
    ) {
        stickers.forEach { sticker ->
            sticker {
                val context = LocalPlatformContext.current
                val image = ImageRequest.Builder(context)
                    .data(sticker.image)
                    .memoryCacheKey(sticker.cacheKey.toString())
                    .diskCacheKey(sticker.cacheKey.toString())
                    .build()

                Box(
                    modifier = Modifier.clickable {
                        holder.createImage(
                            offset = Offset(0f, 0f),
                            imageCacheKey = sticker.cacheKey.toString(),
                            contentDescription = sticker.contentDescription
                        )
                        holder.closeStickerSheet()
                    }
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = sticker.contentDescription,
                    )
                }
            }
        }
    }
}
