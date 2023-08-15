package com.example.mymovieapp.features.details.ui

import android.os.Bundle
import android.view.View
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        super.setUpViews(view, savedInstanceState)
    }

    override fun setUpListeners() {
        super.setUpListeners()
    }

    override fun setUpObservers() {
        super.setUpObservers()
    }
}