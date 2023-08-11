package com.example.mymovieapp.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
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

    private var loadingAnimation : ValueAnimator? = null

    private val dotViews = ArrayList<DotView>()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        clipChildren = false
        clipToPadding = false

        initializeDots()
        initializeAnimation()
    }

    private fun dotViewAnimation (dotView: DotView) : Animator {
        val animatorSetScaleUp = AnimatorSet()
        val animatorSetScaleDown = AnimatorSet()
        val animatorSetDotView = AnimatorSet()
        val scaleXUpAnimation = ObjectAnimator.ofFloat(dotView,"scaleX",1.0f,1.5f).apply {
            duration = 250L
        }
        val scaleXDownAnimation = ObjectAnimator.ofFloat(dotView,"scaleX",1.5f,1.0f).apply {
            duration = 250L
        }
        val scaleYUpAnimation = ObjectAnimator.ofFloat(dotView,"scaleY",1.0f,1.5f).apply {
            duration = 250L
        }
        val scaleYDownAnimation = ObjectAnimator.ofFloat(dotView,"scaleY",1.5f,1.0f).apply {
            duration = 250L
        }

        animatorSetScaleUp.playTogether(
            scaleXUpAnimation,
            scaleYUpAnimation
        )
        animatorSetScaleDown.playTogether(
            scaleXDownAnimation,
            scaleYDownAnimation
        )
        animatorSetDotView.playSequentially(
            animatorSetScaleUp,
            animatorSetScaleDown
        )
        animatorSetDotView.duration = 250L
        return animatorSetDotView
    }

    private fun initializeAnimation(){
        loadingAnimation?.cancel()
        loadingAnimation = ValueAnimator.ofInt(0,numberOfDots)
        loadingAnimation?.addUpdateListener {
            if (it.animatedValue != numberOfDots ){
                val dotView = dotViews[it.animatedValue as Int]
                dotViewAnimation(dotView).start()
            }
        }
        loadingAnimation?.repeatMode = ValueAnimator.RESTART
        loadingAnimation?.repeatCount = ValueAnimator.INFINITE
        loadingAnimation?.duration = 1000L
        loadingAnimation?.interpolator = LinearInterpolator()
    }

    private fun initializeDots() {
        for (i in 0 until numberOfDots) {
            val dot = DotView(context)
            val layoutParams = LayoutParams(dotSize, dotSize)
            layoutParams.setMargins(dotSpace, dotSpace, dotSpace, dotSpace)
            dot.layoutParams = layoutParams
            dotViews.add(dot)
            addView(dot)
        }
    }

    fun setIsLoading(isLoading : Boolean){
        this.isLoading = isLoading
    }

    private fun startAnimation(){
        if (isLoading){
            animationStart()
        }
    }

    private fun animationStart(){
        loadingAnimation?.let {
            if (it.isRunning.not()){
                it.start()
            }
        }
    }

    private fun animationEnd(){
        loadingAnimation?.let {
            if (it.isRunning){
                it.end()
            }
        }
    }

    override fun onAttachedToWindow() {
        startAnimation()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        animationEnd()
        super.onDetachedFromWindow()
    }

    companion object {
        const val DEFAULT_DOTS_COUNT = 3
        const val DEFAULT_DOT_SIZE = 12
        const val DEFAULT_DOT_SPACE = 8
    }
}