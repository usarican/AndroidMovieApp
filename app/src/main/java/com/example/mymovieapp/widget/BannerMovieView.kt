package com.example.mymovieapp.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.CustomBannerMovieContainerBinding
import com.example.mymovieapp.utils.GlideHelpers
import com.example.mymovieapp.utils.extensions.bindImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class BannerMovieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context,attrs,defStyle) {


    private val mBinding : CustomBannerMovieContainerBinding

    private var imageSource : String? = null

    init {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_banner_movie_container,
            this,
            true
        )

        initAttr(attrs)
    }

    private fun initAttr(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BannerMovieView,
            0,0
        ).apply {
            try {
                imageSource = getString(R.styleable.BannerMovieView_imageSource)

                if (!imageSource.isNullOrEmpty()){
                    setBannerMovieImage(imageSource)
                    setShadowColor(imageSource)
                }

            }finally {
                recycle()
            }
        }
    }

    private fun setBannerMovieImage(url : String?){
        imageSource = url

        mBinding.bannerMovieItemImage.bindImage(url)
    }

    private fun setShadowColor(url : String?) {
        imageSource = url

        CoroutineScope(Dispatchers.IO).launch{
            val bitmap = GlideHelpers.getBitmapOfImage(context, url)

            Palette.from(bitmap).generate {
                it?.let { palette ->
                    val dominantColor = palette.getVibrantColor(
                        ContextCompat.getColor(
                            context,
                            R.color.background_color
                        )
                    )

                    mBinding.bannerMovieItemContainer.outlineSpotShadowColor = ColorUtils.setAlphaComponent(dominantColor,255)
                    mBinding.bannerMovieItemContainer.outlineAmbientShadowColor = ColorUtils.setAlphaComponent(dominantColor,255)
                }
            }
        }
    }

    fun setImageResource(url : String){
        imageSource = url
        setBannerMovieImage(url)
        setShadowColor(url)
    }

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}