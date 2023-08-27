package com.example.mymovieapp.features.details.ui


import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailCommentsBinding
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailCommentsAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import com.example.mymovieapp.utils.extensions.toGone
import com.example.mymovieapp.utils.extensions.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
@AndroidEntryPoint
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
        lifecycleScope.launch {
            movieDetailCommentsAdapter.loadStateFlow.collectLatest {
                when (it.refresh){
                    is LoadState.Loading -> {
                        Timber.tag(TAG).d("LoadStateIsTriggered")
                        binding.movieDetailFragmentProgressBar.toVisible()
                        binding.movieDetailCommentsRecyclerview.toGone()
                        binding.notFoundView.root.toGone()
                    }
                    is LoadState.NotLoading -> {
                        delay(1000L)
                        if (movieDetailCommentsAdapter.itemCount < 1) {
                            binding.movieDetailFragmentProgressBar.toGone()
                            binding.movieDetailCommentsRecyclerview.toGone()
                            binding.notFoundView.root.toVisible()
                            binding.notFoundView.notFoundText.text = stringProvider.getString(R.string.movie_detail_comment_empty_list_string)
                        } else {
                            binding.movieDetailFragmentProgressBar.toGone()
                            binding.movieDetailCommentsRecyclerview.toVisible()
                            binding.notFoundView.root.toGone()
                        }
                    }
                    is LoadState.Error -> {
                        delay(1000L)
                        binding.movieDetailFragmentProgressBar.toGone()
                        binding.movieDetailCommentsRecyclerview.toGone()
                        binding.notFoundView.root.toVisible()
                        binding.notFoundView.notFoundText.text = (it.refresh as LoadState.Error).error.message
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = MovieDetailCommentsFragment::class.java.simpleName
    }


}