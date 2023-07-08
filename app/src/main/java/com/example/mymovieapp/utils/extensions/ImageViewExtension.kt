package com.example.mymovieapp.utils.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.bindImage(url : String?){
    if (url != null){
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}