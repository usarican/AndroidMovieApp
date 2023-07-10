package com.example.mymovieapp.utils.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mymovieapp.utils.GlideHelpers

@BindingAdapter("loadImage")
fun ImageView.bindImage(url : String?){
    if (url != null){
        GlideHelpers.setImage(this,url)
    }
}