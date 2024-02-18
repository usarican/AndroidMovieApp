package com.example.mymovieapp.utils.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mymovieapp.utils.GlideHelpers
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("loadImage")
fun ImageView.bindImage(url : String?){
    if (url != null){
        GlideHelpers.setImage(this,url)
    }
}

@BindingAdapter("loadBitmap")
fun ImageView.loadBitmap(bitmap: Bitmap?){
    if (bitmap != null){
        GlideHelpers.setImage(this,bitmap)
    }
}
@BindingAdapter("loadUri")
fun ImageView.loadUri(uri: Uri?){
    if (uri != null){
        GlideHelpers.setImage(this,uri)
    }
}

@BindingAdapter("loadProfileImage")
fun ShapeableImageView.bindProfileImage(url : String){
    GlideHelpers.setProfileImage(this,url)
}

@BindingAdapter("drawableRes")
fun ImageView.drawableRes(@DrawableRes drawableRes: Int?) {
    if ((drawableRes.isNotNull()) and (drawableRes != Resources.ID_NULL)) {
        setImageDrawable(context.getCompatDrawable(drawableRes!!))
    }
}
