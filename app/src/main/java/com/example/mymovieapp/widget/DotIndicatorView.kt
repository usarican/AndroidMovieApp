package com.example.mymovieapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.example.mymovieapp.R
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import com.example.mymovieapp.utils.GlideHelpers
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DotIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : HorizontalScrollView(context,attrs,defStyle) {

    private var viewPager2 : ViewPager2? = null
    private var dotsNum : Int = 0
    private var activeDotIndex : Int = 0
    private var dotSize = DEFAULT_DOT_SIZE.dp
    private val dotMargin = DEFAULT_DOT_MARGIN.dp

    private val dotContainer: LinearLayout

    private var activeColor : Int = R.color.icon_select_color

    init {
        val customDot = DotView(context).apply {
            this.layoutParams = LayoutParams(dotSize,dotSize).apply {
                setMargins(dotMargin,dotMargin,dotMargin,dotMargin)
            }
        }
        val width = customDot.width
        dotContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(width * 5, ViewGroup.LayoutParams.WRAP_CONTENT)

        }

        addView(dotContainer)

    }

    fun setViewPager2(viewPager2: ViewPager2){
        this.viewPager2 = viewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                setActiveDotIndex(position)
                val adapter = viewPager2.adapter as BannerMoviesAdapter
                val movie = adapter.currentList[position]
                setDotViewColor(movie)
                scrollToIndex(position)
            }
        })
        setNumDots(viewPager2.adapter?.itemCount ?: 0)
    }

    private fun setNumDots(numOfDots : Int) {
        this.dotsNum = numOfDots
        update()
    }

    private fun setActiveDotIndex(index : Int){
        this.activeDotIndex = index
        update()
    }

    private fun update() {
        dotContainer.removeAllViews()

        for (i in 0 until dotsNum){
            val dot = DotView(context).apply {
                this.setActiveColor(activeColor)
                if (i == activeDotIndex) this.setIsActive(true) else this.setIsActive(false)
                this.layoutParams = LayoutParams(dotSize,dotSize).apply {
                    setMargins(dotMargin,dotMargin,dotMargin,dotMargin)
                }
            }
            dotContainer.addView(dot)
        }
    }

    private fun scrollToIndex(index: Int) {
        val item = dotContainer.getChildAt(index)
        val scrollToX = item.left - (this.width - item.width) / 2
        this.smoothScrollTo(scrollToX, 0)
    }

    private fun setDotViewColor(movieItem : Movie) {
        CoroutineScope(Dispatchers.IO).launch{
            val bitmap = GlideHelpers.getBitmapOfImage(context, movieItem.image)
            Palette.from(bitmap).generate {
                it?.let { palette ->
                    val dominantColor = palette.getVibrantColor(
                        ContextCompat.getColor(
                            context,
                            R.color.background_color
                        )
                    )
                    setActiveColor(dominantColor)
                    update()
                }
            }
        }
    }

    private fun setActiveColor(color : Int) {
        this.activeColor = color
    }

    companion object {
        const val DEFAULT_DOT_SIZE = 12
        const val DEFAULT_DOT_MARGIN = 6
        const val MAX_VISIBLE_ITEM_COUNT = 5
    }

}