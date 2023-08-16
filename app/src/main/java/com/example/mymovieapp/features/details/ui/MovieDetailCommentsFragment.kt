package com.example.mymovieapp.features.details.ui

import androidx.fragment.app.viewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailCommentsBinding

class MovieDetailCommentsFragment : BaseFragment<FragmentMovieDetailCommentsBinding>(R.layout.fragment_movie_detail_comments) {

    val viewModel : MovieDetailViewModel by viewModels({requireParentFragment()})
}