package com.takealook.memento.sticker

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MementoStickerListSheet(
    columns: GridCells = GridCells.Fixed(3),
    onDismiss: () -> Unit,
    builder: MementoStickerBuilder.() -> Unit
) {
    val stickers = remember {
        MementoStickerBuilder().apply(builder).build()
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyVerticalGrid(columns = columns) {
            items(stickers) { sticker ->
                sticker.invoke(this)
            }
        }
    }
}
