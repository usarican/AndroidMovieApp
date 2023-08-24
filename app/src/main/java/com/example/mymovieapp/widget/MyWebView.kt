package com.example.mymovieapp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.mymovieapp.R

@SuppressLint("SetJavaScriptEnabled")
class MyWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var videoKey : String? = null

    init {
        initAttrs(attrs)
        loadYoutubeVideo(videoKey)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyWebView,
            0, 0).apply {

            try {
                videoKey = getString(R.styleable.MyWebView_videoKey)
            } finally {
                recycle()
            }
        }
    }

    fun loadYoutubeVideo(videoId: String?) {
        val embeddedHtml = """
    <!DOCTYPE html>
    <html>
    <body>
    
    <iframe
        width="100%"
        height=""
        src="https://www.youtube.com/embed/$videoId"
        frameborder="0"
        allowfullscreen>
    </iframe>
    
    </body>
    </html>
""".trimIndent()
        settings.javaScriptEnabled = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        this.loadData(embeddedHtml, "text/html", "utf-8")
    }
}