package com.example.mymovieapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GlideHelpers {

    fun setImage(imageView: ImageView, source : String?) {
        Glide.with(imageView.context).load(source).error(R.drawable.ic_error_with_running).into(imageView)
    }

    fun getBitmapOfImage(context : Context, source: String?): Bitmap =
        Glide.with(context).asBitmap().load(source).error(R.drawable.ic_error_with_running).submit(BITMAP_WIDTH,BITMAP_HEIGHT).get()

    private const val BITMAP_HEIGHT = 400
    private const val BITMAP_WIDTH = 400
}