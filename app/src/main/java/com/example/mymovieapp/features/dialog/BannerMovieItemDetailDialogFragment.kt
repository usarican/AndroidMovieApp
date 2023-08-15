package com.example.mymovieapp.features.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseDialogFragment
import com.example.mymovieapp.databinding.DialogLayoutBannerMovieDetailsBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.features.details.ui.adapter.BannerMovieGenreListAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp


class BannerMovieItemDetailDialogFragment : BaseDialogFragment<DialogLayoutBannerMovieDetailsBinding>(R.layout.dialog_layout_banner_movie_details) {

    private var movieDetailItem : MovieDetail? = null
    private val movieGenreListAdapter : BannerMovieGenreListAdapter by lazy {
        BannerMovieGenreListAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MOVIE_DETAIL_ITEM,movieDetailItem)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = savedInstanceState ?: arguments
        bundle?.let {
            movieDetailItem = it.getParcelable(MOVIE_DETAIL_ITEM)
        }
        super.onCreate(savedInstanceState)
    }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        binding.movieDetail = movieDetailItem
        setupRecyclerView()
        binding.container.setOnClickListener {
            this.dismiss()
        }
    }

    override fun setUpUI() {
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            setDimAmount(0.9F)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    private fun setupRecyclerView(){
        binding.apply {
            genreListRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL,false)
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

        fun newInstance(movieDetailItem : MovieDetail) =
            BannerMovieItemDetailDialogFragment().apply {
                arguments = bundleOf(
                    MOVIE_DETAIL_ITEM to movieDetailItem
                )
            }
    }
}