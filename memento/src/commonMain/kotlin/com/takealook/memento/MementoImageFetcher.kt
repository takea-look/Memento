package com.takealook.memento

import coil3.ImageLoader
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.memory.MemoryCache
import coil3.request.Options
import com.takealook.memento.sticker.Key

class StickerKeyFetcher(
    private val key: Key,
    private val imageLoader: ImageLoader
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val memoryCache = imageLoader.memoryCache
        val memorySnapshot = memoryCache?.get(MemoryCache.Key(key.toString()))

        return ImageFetchResult(
            image = memorySnapshot?.image!!,
            dataSource = coil3.decode.DataSource.MEMORY_CACHE,
            isSampled = false,
        )
    }

    class Factory : Fetcher.Factory<Key> {
        override fun create(data: Key, options: Options, imageLoader: ImageLoader): Fetcher? {
            return StickerKeyFetcher(data, imageLoader)
        }
    }
}
