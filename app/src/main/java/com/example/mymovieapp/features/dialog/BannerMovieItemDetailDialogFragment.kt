package com.example.mymovieapp.features.dialog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseDialogFragment
import com.example.mymovieapp.databinding.DialogLayoutBannerMovieDetailsBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.features.details.ui.adapter.BannerMovieGenreListAdapter
import com.example.mymovieapp.features.home.ui.HomeFragmentDirections
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.GlideHelpers
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BannerMovieItemDetailDialogFragment :
    BaseDialogFragment<DialogLayoutBannerMovieDetailsBinding>(R.layout.dialog_layout_banner_movie_details) {

    private var movieDetailItem: MovieDetail? = null
    private var clickListener: MyClickListeners<Int>? = null
    private val movieGenreListAdapter: BannerMovieGenreListAdapter by lazy {
        BannerMovieGenreListAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MOVIE_DETAIL_ITEM, movieDetailItem)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay)
        val bundle = savedInstanceState ?: arguments
        bundle?.let {
            movieDetailItem = it.getParcelable(MOVIE_DETAIL_ITEM)
        }
        super.onCreate(savedInstanceState)
    }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        binding.movieDetail = movieDetailItem
        setupRecyclerView()
        binding.imageContainer.setOnClickListener {
            this.dismiss()
        }
        binding.moreInformationButton.setOnClickListener {
            dismiss()
            clickListener?.click(movieDetailItem?.id ?: -1)
        }
    }

    fun setClickListener(clickListeners: MyClickListeners<Int>) {
        this.clickListener = clickListeners
    }

    override fun setUpUI() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val context = activity?.applicationContext
                context?.let {
                    val bitmap = GlideHelpers.getBitmapOfImage(context, movieDetailItem?.image)

                    val bitmapDrawable = BitmapDrawable(bitmap)
                    bitmapDrawable.alpha = 100

                    Palette.from(bitmap).generate {
                        it?.let { palette ->
                            val dominantColor = palette.getVibrantColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.background_color
                                )
                            )

                            val color = ColorUtils.setAlphaComponent(dominantColor, 25)

                            binding.container.setBackgroundColor(color)
                        }
                    }

                    binding.bannerMovieDetailBackgroundImage.setImageDrawable(bitmapDrawable)
                }
            } catch (e: Exception) {
                return@launch
            }
        }
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            setDimAmount(0.9F)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            genreListRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            genreListRecyclerView.adapter = movieGenreListAdapter
            movieGenreListAdapter.setData(movieDetailItem?.genres?.take(2) ?: emptyList())
            genreListRecyclerView.addItemDecoration(
                EqualSpacingItemDecoration(
                    8.dp,
                    EqualSpacingItemDecoration.HORIZONTAL,
                )
            )
        }
    }

    companion object {
        private const val MOVIE_DETAIL_ITEM = "movieDetailItem"

        fun newInstance(movieDetailItem: MovieDetail) =
            BannerMovieItemDetailDialogFragment().apply {
                arguments = bundleOf(
                    MOVIE_DETAIL_ITEM to movieDetailItem
                )
            }
    }
}