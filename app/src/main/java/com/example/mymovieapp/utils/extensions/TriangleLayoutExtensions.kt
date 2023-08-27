package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import com.example.mymovieapp.widget.TriangleLayout

@BindingAdapter("lineColor")
fun imageUrlForLineColor(view : TriangleLayout,imageUrl : String?){
    if (imageUrl != null){
        view.setImageUrlForLineColor(imageUrl)
    }
}