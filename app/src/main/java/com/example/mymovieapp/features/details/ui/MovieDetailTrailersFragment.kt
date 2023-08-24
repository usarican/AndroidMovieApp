package com.example.mymovieapp.features.details.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailTrailersBinding
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailTrailerAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.launch
import timber.log.Timber


class MovieDetailTrailersFragment : BaseFragment<FragmentMovieDetailTrailersBinding>(R.layout.fragment_movie_detail_trailers) {

    private val viewModel : MovieDetailViewModel by viewModels({requireParentFragment()})

    private val movieDetailTrailerAdapter : MovieDetailTrailerAdapter by lazy {
        MovieDetailTrailerAdapter(viewLifecycleOwner.lifecycle)
    }

    override fun setUpUI() {
        binding.movieDetailTrailerRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.movieDetailTrailerRecyclerView.adapter = movieDetailTrailerAdapter
        binding.movieDetailTrailerRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                8.dp,
                EqualSpacingItemDecoration.VERTICAL
            )
        )
    }

    override fun setUpObservers() {
        viewModel.getMovieDetailInformationLiveData().observe(viewLifecycleOwner){ movieDetailPageItem ->
            movieDetailPageItem.movieTrailers.let {
                    movieDetailTrailerAdapter.setData(it as List<Any>)
            }
        }
    }
}