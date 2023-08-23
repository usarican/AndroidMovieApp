package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import com.example.mymovieapp.widget.MyWebView
@BindingAdapter("setVideo")
fun setVideoId(webView : MyWebView, videoId : String?) {
    webView.loadYoutubeVideo(videoId)
}