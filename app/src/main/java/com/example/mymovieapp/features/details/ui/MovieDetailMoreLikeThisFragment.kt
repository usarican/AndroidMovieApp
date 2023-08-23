package com.example.mymovieapp.features.details.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailMoreLikeThisBinding
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailSimilarMovieAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.launch

class MovieDetailMoreLikeThisFragment : BaseFragment<FragmentMovieDetailMoreLikeThisBinding>(R.layout.fragment_movie_detail_more_like_this){
    private val viewModel : MovieDetailViewModel by viewModels({requireParentFragment()})

    private val movieDetailSimilarMovieAdapter : MovieDetailSimilarMovieAdapter by lazy {
        MovieDetailSimilarMovieAdapter()
    }

    private val myClickListener = object : MyClickListeners<Int> {
        override fun click(item: Int) {
            viewModel.getMovieDetailInformation(item,"en")
        }

    }

    override fun setUpUI() {
        binding.movieDetailSimilarMovieRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        movieDetailSimilarMovieAdapter.setItemClickListener(myClickListener)
        binding.movieDetailSimilarMovieRecyclerView.adapter = movieDetailSimilarMovieAdapter
        binding.movieDetailSimilarMovieRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                8.dp,
                EqualSpacingItemDecoration.VERTICAL
            )
        )
    }

    override fun setUpObservers() {
        viewModel.getMovieDetailInformationLiveData().observe(viewLifecycleOwner){ movieDetailPageItem ->
            movieDetailPageItem.movieMoreLikeThis?.let {
                lifecycleScope.launch {
                    movieDetailSimilarMovieAdapter.submitData(it)
                }
            }
        }
    }
}