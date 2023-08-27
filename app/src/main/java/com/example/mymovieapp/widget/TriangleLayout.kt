package com.example.mymovieapp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

import com.example.mymovieapp.R
import com.example.mymovieapp.utils.GlideHelpers
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TriangleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context,attrs,defStyle) {

    var fillColor = resources.getColor(R.color.white, null)
    var lineColor = resources.getColor(R.color.black,null)

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = fillColor
    }
    val path = Path()

    fun setImageUrlForLineColor(imageUrl : String){
        setLineColor(imageUrl)
    }

    private fun setLineColor(url : String?) {

        CoroutineScope(Dispatchers.IO).launch{
            try {
                val bitmap = GlideHelpers.getBitmapOfImage(context, url)

                Palette.from(bitmap).generate {
                    it?.let { palette ->
                        val dominantColor = palette.getVibrantColor(
                            ContextCompat.getColor(
                                context,
                                R.color.background_color
                            )
                        )
                        lineColor = dominantColor
                        invalidate()
                    }
                }
            }catch (e:Exception){
                return@launch
            }
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.setAlphaComponent(lineColor,255)
            strokeWidth = 15f
        }
        path.moveTo(0f, 0f)
        path.lineTo(0f, height.toFloat())
        path.lineTo(width.toFloat(),height.toFloat())
        path.lineTo(width.toFloat(),((height * 0.5).toFloat()))
        canvas.drawPath(path, fillPaint)
        canvas.drawLine(0f,0f,width.toFloat(),((height * 0.5).toFloat()),linePaint)
    }
}