package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import com.example.mymovieapp.widget.LoadingDotView

@BindingAdapter("isLoading")
fun setIsLoading(view : LoadingDotView,isLoading : Boolean){
    view.setIsLoading(isLoading)
}