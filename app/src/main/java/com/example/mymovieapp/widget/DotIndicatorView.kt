package com.example.mymovieapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
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
import timber.log.Timber

class DotIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context,attrs,defStyle) {

    private var viewPager2 : ViewPager2? = null
    private var dotsNum : Int = 0
    private var activeDotIndex : Int = 0
    private var dotSize = DEFAULT_DOT_SIZE.dp
    private val dotMargin = DEFAULT_DOT_MARGIN.dp

    private var activeColor : Int = R.color.icon_select_color


    init {

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val itemWidth = getChildAt(0)?.measuredWidth ?: 0
        val itemHeight = getChildAt(0)?.measuredHeight ?: 0
        val height = itemHeight * 1.5
        val totalWidth = itemWidth * MAX_VISIBLE_ITEM_COUNT * 2

        setMeasuredDimension(totalWidth,height.toInt())
    }

    fun setViewPager2(viewPager2: ViewPager2){
        this.viewPager2 = viewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                setActiveDotIndex(position)
                val adapter = viewPager2.adapter as BannerMoviesAdapter
                val movie = adapter.currentList[position]
                setDotViewColor(movie)
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
        this.removeAllViews()

        for (i in 0 until MAX_VISIBLE_ITEM_COUNT){
            val dot = DotView(context).apply {
                this.setActiveColor(activeColor)
                if (i == activeDotIndex % 5) {
                    this.setIsActive(true)
                    this.animateSelection(true)
                } else this.setIsActive(false)
                this.layoutParams = LayoutParams(dotSize,dotSize).apply {
                    setMargins(dotMargin,dotMargin,dotMargin,dotMargin)
                }
            }
            this.addView(dot)
        }
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