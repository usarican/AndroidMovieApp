package com.example.mymovieapp.utils.extensions

import android.graphics.Bitmap

fun Bitmap.scale(maxWidthAndHeight: Int): Bitmap {
    val newWidth: Int
    val newHeight: Int

    if (this.width >= this.height) {
        val ratio: Float = this.width.toFloat() / this.height.toFloat()

        newWidth = maxWidthAndHeight
        // Calculate the new height for the scaled bitmap
        newHeight = Math.round(maxWidthAndHeight / ratio)
    } else {
        val ratio: Float = this.height.toFloat() / this.width.toFloat()

        // Calculate the new width for the scaled bitmap
        newWidth = Math.round(maxWidthAndHeight / ratio)
        newHeight = maxWidthAndHeight
    }

    return Bitmap.createScaledBitmap(
        this,
        newWidth,
        newHeight,
        false
    )
}