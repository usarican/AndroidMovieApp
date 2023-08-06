package com.example.mymovieapp.widget

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils
import com.example.mymovieapp.R
import kotlin.math.min

class DotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context,attrs,defStyle) {

    // Properties
    private val paint : Paint = Paint().apply { isAntiAlias = true }
    private var activeColor : Int = 0
    private val inActiveColor : Int = R.color.icon_unselect_color

    private var isActive : Boolean = false
    private var heightCircle = 0
    private var circleCenter = 0F

    private val animDuration = 150L


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val usableWidth = measure(widthMeasureSpec) - (paddingLeft + paddingRight)
        val usableHeight = measure(heightMeasureSpec) - (paddingTop + paddingBottom)
        heightCircle = min(usableWidth, usableHeight)
        setMeasuredDimension(heightCircle, heightCircle)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    private fun measure(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize // The parent has determined an exact size for the child.
            MeasureSpec.AT_MOST -> specSize // The child can be as large as it wants up to the specified size.
            else -> heightCircle // The parent has not imposed any constraint on the child.
        }
    }

    private fun update(){
        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)
        heightCircle = min(usableWidth, usableHeight)

        circleCenter = (heightCircle.toFloat() * 2) / 2
        invalidate()
    }

    fun setIsActive(isActive : Boolean){
        this.isActive = isActive
        invalidate()
    }

    fun setActiveColor(color: Int){
        this.activeColor = color
        invalidate()
    }

    fun animateSelection(selected: Boolean) {
        val scale = if (selected) 1.5f else 1.0f
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", scale),
            PropertyValuesHolder.ofFloat("scaleY", scale)
        )
        animator.duration = animDuration
        animator.start()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val radius = width / 2.toFloat()
        val centerY = height / 2.toFloat()

        if (isActive){
            paint.color = ColorUtils.setAlphaComponent(activeColor,255)
        } else {
            paint.color = resources.getColor(inActiveColor)
        }
        canvas?.drawCircle(
            radius,
            centerY,
            radius,
            paint
        )
    }

}