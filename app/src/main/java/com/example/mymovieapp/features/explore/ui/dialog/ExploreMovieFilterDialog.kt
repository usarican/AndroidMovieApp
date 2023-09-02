package com.example.mymovieapp.features.explore.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseBottomSheetFragment
import com.example.mymovieapp.databinding.MovieFilterDialogLayoutBinding
import com.example.mymovieapp.features.explore.ui.adapter.ExploreMovieFilterAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreMovieFilterDialog : BaseBottomSheetFragment<MovieFilterDialogLayoutBinding>(R.layout.movie_filter_dialog_layout) {

    private val viewModel : ExploreMovieFilterDialogViewModel by viewModels()
    private lateinit var  genreFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  sortFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  regionFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  periodFilterAdapter : ExploreMovieFilterAdapter


    override fun setUpObservers() {
        lifecycleScope.launch(SupervisorJob()) {
            viewModel.apply {
                launch {
                    getMovieFilterGenreItemList().collectLatest {
                        genreFilterAdapter.submitList(it)
                    }
                }

                launch {
                    getMovieFilterPeriodItemList().collectLatest {
                        periodFilterAdapter.submitList(it)
                    }
                }

                launch {
                    getMovieFilterSortItemList().collectLatest {
                        sortFilterAdapter.submitList(it)
                    }
                }

                launch {
                    getMovieFilterRegionItemList().collectLatest {
                        regionFilterAdapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun setUpListeners() {
        super.setUpListeners()
    }

    override fun setUpUI() {
        binding.viewModel = viewModel
        setUpRecyclerViews()
        viewModel.getGenreList("en")
        viewModel.initFilterItems()
    }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {

    }


    private fun setUpRecyclerViews(){
        genreFilterAdapter = ExploreMovieFilterAdapter(viewModel.myClickListener)
        sortFilterAdapter = ExploreMovieFilterAdapter(viewModel.myClickListener)
        regionFilterAdapter = ExploreMovieFilterAdapter(viewModel.myClickListener)
        periodFilterAdapter = ExploreMovieFilterAdapter(viewModel.myClickListener)

        binding.filterDialogGenreRecyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.filterDialogRegionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.filterDialogTimePeriodRecyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.filterDialogSortRecyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        binding.filterDialogGenreRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                4.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogRegionsRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                4.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogTimePeriodRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                4.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogSortRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                4.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogGenreRecyclerView.setHasFixedSize(true)
        binding.filterDialogRegionsRecyclerView.setHasFixedSize(true)
        binding.filterDialogTimePeriodRecyclerView.setHasFixedSize(true)
        binding.filterDialogSortRecyclerView.setHasFixedSize(true)

        binding.filterDialogGenreRecyclerView.adapter = genreFilterAdapter
        binding.filterDialogRegionsRecyclerView.adapter = regionFilterAdapter
        binding.filterDialogTimePeriodRecyclerView.adapter = periodFilterAdapter
        binding.filterDialogSortRecyclerView.adapter = sortFilterAdapter

    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    companion object {

    }
}