package com.example.mymovieapp.features.auth.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentSetupProfileSection1Binding
import com.example.mymovieapp.features.explore.ui.adapter.ExploreMovieFilterAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SetupProfileSection1Fragment : BaseFragment<FragmentSetupProfileSection1Binding>(R.layout.fragment_setup_profile_section1) {

    val viewModel : AuthenticationViewModel by activityViewModels()

    private lateinit var  genreFilterAdapter : ExploreMovieFilterAdapter

    override fun setUpObservers() {
        lifecycleScope.launch(SupervisorJob()) {
            launch {
                viewModel.getMovieGenreListStateFlow().collectLatest {
                    genreFilterAdapter.submitList(it)
                }
            }
        }
    }

    override fun setUpUI() {
        viewModel.getGenreList("en", emptyList())
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        genreFilterAdapter = ExploreMovieFilterAdapter(viewModel.genreListClickListener)
        binding.setupSection1RecyclerView.layoutManager = GridLayoutManager(requireContext(),2,RecyclerView.VERTICAL,false)
        binding.setupSection1RecyclerView.adapter = genreFilterAdapter

        binding.setupSection1RecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                4.dp, EqualSpacingItemDecoration.GRID,false
            )
        )
        binding.setupSection1RecyclerView.setHasFixedSize(true)
    }

}