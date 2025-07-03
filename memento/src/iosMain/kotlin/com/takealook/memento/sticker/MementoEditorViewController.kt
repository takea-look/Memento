package com.takealook.memento.sticker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.window.ComposeUIViewController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.takealook.memento.MementoEditor
import com.takealook.memento.StickerKeyFetcher
import com.takealook.memento.createImage
import com.takealook.memento.rememberMementoStateHolder
import platform.UIKit.UIImage

@OptIn(ExperimentalMaterial3Api::class)
fun MementoEditorViewController(
    mainContent: UIImage,
    stickerBuilder: MementoStickerBuilder<MementoSticker<UIImage>>.() -> Unit = {}
) = ComposeUIViewController {
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

    val stickers = remember {
        MementoStickerBuilder<MementoSticker<UIImage>>()
            .apply(stickerBuilder)
            .build()
    }

    val context = LocalPlatformContext.current
    val holder = rememberMementoStateHolder()

    MementoEditor(
        stateHolder = holder,
        mainContent = {
            AsyncImage(
                model = mainContent.getBytes(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        stickers.forEach { sticker ->
            sticker {
                val image = ImageRequest.Builder(context)
                    .data(sticker.image.getBytes())
                    .memoryCacheKey(sticker.cacheKey.toString())
                    .diskCacheKey(sticker.cacheKey.toString())
                    .build()

                AsyncImage(
                    model = image,
                    contentDescription = sticker.contentDescription ?: sticker.cacheKey.toString(),
                    modifier = Modifier.clickable {
                        holder.createImage(
                            offset = Offset(0f, 0f),
                            imageCacheKey = sticker.cacheKey.toString()
                        )
                        holder.closeStickerSheet()
                    }
                )
            }
        }
    }
}
