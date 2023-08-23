package com.example.mymovieapp.utils.extensions

import android.content.res.Resources
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
