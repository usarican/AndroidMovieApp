package com.example.mymovieapp.features.details.ui

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailTrailersBinding
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailTrailerAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import com.example.mymovieapp.utils.extensions.toGone
import com.example.mymovieapp.utils.extensions.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                if (it.isNullOrEmpty()){
                    binding.movieDetailTrailerRecyclerView.toGone()
                    binding.notFoundView.root.toVisible()
                    binding.notFoundView.notFoundText.text = stringProvider.getString(R.string.movie_detail_trailer_empty_list_string)
                }else {
                    binding.movieDetailTrailerRecyclerView.toVisible()
                    binding.notFoundView.root.toGone()
                    movieDetailTrailerAdapter.setData(it as List<Any>)
                }
            }
        }
    }
}