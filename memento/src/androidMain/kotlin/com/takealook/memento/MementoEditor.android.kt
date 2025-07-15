package com.takealook.memento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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

@Composable
fun MementoEditor(
    mainContent: @Composable BoxScope.() -> Unit,
) {
    val holder = rememberMementoStateHolder()

    MementoEditor(
        stateHolder = holder,
        mainContent = mainContent,
        modifier = Modifier.fillMaxSize()
    )
}
