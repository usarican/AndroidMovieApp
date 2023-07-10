package com.example.mymovieapp.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ViewPagerTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val r = 1 - kotlin.math.abs(position)
        page.scaleY = 0.85f + r * 0.15f
    }
}