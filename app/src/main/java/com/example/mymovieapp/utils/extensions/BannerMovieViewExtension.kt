package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import com.example.mymovieapp.widget.BannerMovieView

@BindingAdapter("getImageSource")
fun loadImageSource(view : BannerMovieView, url : String?){
    url?.let {
        view.setImageResource(it)
    }
}