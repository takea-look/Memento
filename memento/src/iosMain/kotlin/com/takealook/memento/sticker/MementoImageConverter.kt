package com.takealook.memento.sticker

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
fun UIImage.getBytes(): ByteArray {
    val pngRepresentation = UIImagePNGRepresentation(this)!!
    val byteArray = ByteArray(pngRepresentation.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), pngRepresentation.bytes, pngRepresentation.length)
        }
    }
    return byteArray
}