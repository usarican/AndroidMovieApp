package com.example.mymovieapp.features.details.ui

import androidx.fragment.app.viewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentMovieDetailMoreLikeThisBinding

class MovieDetailMoreLikeThisFragment : BaseFragment<FragmentMovieDetailMoreLikeThisBinding>(R.layout.fragment_movie_detail_more_like_this){
    val viewModel : MovieDetailViewModel by viewModels({requireParentFragment()})

}