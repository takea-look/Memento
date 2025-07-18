package com.takealook.memento

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val width = this.width
    val height = this.height
    val argbBuffer = IntArray(width * height)
    this.readPixels(argbBuffer)

    val rgbaBuffer = ByteArray(argbBuffer.size * 4)
    for (i in argbBuffer.indices) {
        val argb = argbBuffer[i]
        val a = (argb shr 24) and 0xFF
        val r = (argb shr 16) and 0xFF
        val g = (argb shr 8) and 0xFF
        val b = argb and 0xFF

        rgbaBuffer[i * 4 + 0] = r.toByte()
        rgbaBuffer[i * 4 + 1] = g.toByte()
        rgbaBuffer[i * 4 + 2] = b.toByte()
        rgbaBuffer[i * 4 + 3] = a.toByte()
    }

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = rgbaBuffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = (CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or 0u)
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}