package com.example.mymovieapp.features.explore.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseBottomSheetFragment
import com.example.mymovieapp.databinding.MovieFilterDialogLayoutBinding
import com.example.mymovieapp.features.explore.ui.ExploreFragment
import com.example.mymovieapp.features.explore.ui.adapter.ExploreMovieFilterAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExploreMovieFilterDialog() : BaseBottomSheetFragment<MovieFilterDialogLayoutBinding>(R.layout.movie_filter_dialog_layout) {

    @Inject
    lateinit var movieFilterUtils: MovieFilterUtils

    private val viewModel : ExploreMovieFilterDialogViewModel by viewModels()
    private lateinit var  genreFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  sortFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  regionFilterAdapter : ExploreMovieFilterAdapter
    private lateinit var  periodFilterAdapter : ExploreMovieFilterAdapter

    private var movieFilterItem : MovieFilterItem? = null
    private var requestKey : String = ""
    private var genreList : HashMap<Int,String> = hashMapOf()


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MOVIE_FILTER_ITEM,movieFilterItem)
        outState.putString(REQUEST_KEY,requestKey)
        outState.putSerializable(GENRE_LIST_ITEM,genreList)
        super.onSaveInstanceState(outState)
    }
    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        val bundle = savedInstanceState ?: arguments
        bundle?.let {
            movieFilterItem = it.getParcelable(MOVIE_FILTER_ITEM) ?: movieFilterUtils.getInitialMovieFilterItem()
            requestKey= it.getString(REQUEST_KEY,"")
            genreList = it.getSerializable(GENRE_LIST_ITEM) as HashMap<Int, String>
        }
    }



    override fun setUpObservers() {
        viewModel.getMovieFilterItem().observe(viewLifecycleOwner) {
            Timber.tag(TAG).d("Collected Filter Item $it")
            viewModel.getGenreList("en",it)
            viewModel.initFilterItems(it)
        }
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
        binding.apply {
            resetButton.setOnClickListener {
                viewModel?.resetFilter()
            }
            applyButton.setOnClickListener {
                returnFragmentResult()
                dismiss()
            }

        }
    }

    override fun setUpUI() {
        binding.viewModel = viewModel
        viewModel.setSavedMovieFilterItem(movieFilterItem ?: movieFilterUtils.getInitialMovieFilterItem())
        viewModel.setGenreList(genreList)
        setUpRecyclerViews()
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
                2.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogRegionsRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                2.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogTimePeriodRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                2.dp,EqualSpacingItemDecoration.HORIZONTAL,false
            )
        )

        binding.filterDialogSortRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                2.dp,EqualSpacingItemDecoration.HORIZONTAL,false
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

    private fun returnFragmentResult(){
        val result = viewModel.applyFilter()
        setFragmentResult(
            requestKey,
            bundleOf(FRAGMENT_RESULT_MOVIE_FILTER_ITEM to result)
        )
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    companion object {

        private const val MOVIE_FILTER_ITEM = "movieFilterItem"
        const val FRAGMENT_RESULT_MOVIE_FILTER_ITEM = "FRAGMENT_RESULT_MOVIE_FILTER_ITEM"
        const val GENRE_LIST_ITEM = "GENRE_LIST_ITEM"
        const val REQUEST_KEY = "REQUEST_KEY"
        private val TAG = ExploreMovieFilterDialog::class.java.simpleName
        fun newInstance(
            movieFilterItem : MovieFilterItem,
            requestKey : String,
            genreList : Map<Int,String>
        ) = ExploreMovieFilterDialog().apply {
            arguments = bundleOf(
                MOVIE_FILTER_ITEM to movieFilterItem,
                REQUEST_KEY to requestKey,
                GENRE_LIST_ITEM to genreList
            )
        }
    }
}