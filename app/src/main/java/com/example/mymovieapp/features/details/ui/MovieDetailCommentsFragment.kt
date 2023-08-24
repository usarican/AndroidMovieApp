package com.example.mymovieapp.features.details.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailCommentsBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailReview
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailCommentsAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieDetailCommentsFragment : BaseFragment<FragmentMovieDetailCommentsBinding>(R.layout.fragment_movie_detail_comments) {

    private val viewModel : MovieDetailViewModel by viewModels({requireParentFragment()})

    private val movieDetailCommentsAdapter : MovieDetailCommentsAdapter by lazy {
        MovieDetailCommentsAdapter()
    }

    override fun setUpUI() {
        binding.movieDetailCommentsRecyclerview.layoutManager = LinearLayoutManager(context, VERTICAL,false)
        binding.movieDetailCommentsRecyclerview.adapter = movieDetailCommentsAdapter
        binding.movieDetailCommentsRecyclerview.addItemDecoration(
            EqualSpacingItemDecoration(
                8.dp,
                EqualSpacingItemDecoration.VERTICAL
            )
        )
    }

    override fun setUpObservers() {
        viewModel.getMovieDetailInformationLiveData().observe(viewLifecycleOwner){ movieDetailPageItem ->
            movieDetailPageItem.movieReviews?.let {
                lifecycleScope.launch {
                    movieDetailCommentsAdapter.submitData(it)
                }
            }
        }
    }


}