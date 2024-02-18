package com.example.mymovieapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mymovieapp.R
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GlideHelpers {

    fun setImage(imageView: ImageView, source : String?) {
        Glide.with(imageView.context).load(source).error(R.drawable.ic_error)
            .into(imageView)
    }


    fun setProfileImage(imageView: ShapeableImageView, source : String) {
        val sourceLast4String = source.takeLast(4)
        if (sourceLast4String != "null") {
            Glide.with(imageView.context).load(source).placeholder(R.drawable.user_photo)
                .error(R.drawable.ic_error_with_running).into(imageView)
        } else {
            Glide.with(imageView.context).load(R.drawable.user_photo).into(imageView)
        }

    }

    fun getBitmapOfImage(context : Context, source: String?): Bitmap =
        Glide.with(context).asBitmap().load(source).error(R.drawable.ic_error_with_running).submit(BITMAP_WIDTH,BITMAP_HEIGHT).get()

    fun setImage(imageView: ImageView,bitmap: Bitmap) {
        Glide
            .with(imageView.context)
            .load(bitmap)
            .error(R.drawable.ic_error_with_running)
            .placeholder(R.drawable.user_photo)
            .into(imageView)
    }

    fun setImage(imageView: ImageView,uri: Uri) {
        Glide
            .with(imageView.context)
            .load(uri)
            .error(R.drawable.ic_error_with_running)
            .placeholder(R.drawable.user_photo)
            .into(imageView)
    }

    private const val BITMAP_HEIGHT = 400
    private const val BITMAP_WIDTH = 400
}