package com.example.mymovieapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.example.mymovieapp.R
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.*
import timber.log.Timber

class LoadingDotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context,attrs,defStyle) {

    private var dotSize = DEFAULT_DOT_SIZE.dp
    private var dotSpace = DEFAULT_DOT_SPACE.dp
    private var numberOfDots = DEFAULT_DOTS_COUNT
    private var isLoading : Boolean = true

    private var currentIndex = 0
    private var animationJob : Job? = null
    private val scaleUpAnimation: Animation = createScaleUpAnimation(250)
    private val scaleDownAnimation: Animation = createScaleDownAnimation(250)

    private val dotViews = ArrayList<DotView>()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        clipChildren = false
        clipToPadding = false

        initializeDots()
    }

    private fun initializeAnimation() {
        animationJob = CoroutineScope(Dispatchers.IO).launch {
            while (isLoading){
                dotViews[currentIndex].startAnimation(scaleDownAnimation)
                delay(250L)

                currentIndex = (currentIndex + 1) % dotViews.size
                Timber.tag("LoadingDot").d(currentIndex.toString())

                dotViews[currentIndex].startAnimation(scaleUpAnimation)
                delay(250L)
            }
        }
    }

    private fun initializeDots() {
        animationJob?.cancel()
        for (i in 0 until numberOfDots) {
            val dot = DotView(context)
            val layoutParams = LayoutParams(dotSize, dotSize)
            layoutParams.setMargins(dotSpace, dotSpace, dotSpace, dotSpace)
            dot.layoutParams = layoutParams
            dotViews.add(dot)
            addView(dot)
        }
        initializeAnimation()
    }

    private fun createScaleUpAnimation(duration: Long): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(
            1f, // From x-scale (start scale)
            1.5f, // To x-scale (end scale)
            1f, // From y-scale (start scale)
            1.5f, // To y-scale (end scale)
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (centered horizontally)
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (centered vertically)
        )
        scaleAnimation.duration = duration
        return scaleAnimation
    }

    private fun createScaleDownAnimation(duration: Long): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(
            1.5f, // From x-scale (start scale)
            1f, // To x-scale (end scale)
            1.5f, // From y-scale (start scale)
            1f, // To y-scale (end scale)
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (centered horizontally)
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (centered vertically)
        )
        scaleAnimation.duration = duration
        return scaleAnimation
    }

    fun setIsLoading(isLoading : Boolean){
        this.isLoading = isLoading
    }

    override fun onDetachedFromWindow() {
        animationJob?.cancel()
        super.onDetachedFromWindow()
    }

    companion object {
        const val DEFAULT_DOTS_COUNT = 3
        const val DEFAULT_DOT_SIZE = 12
        const val DEFAULT_DOT_SPACE = 8
    }
}