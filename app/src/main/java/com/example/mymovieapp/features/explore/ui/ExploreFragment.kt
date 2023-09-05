package com.example.mymovieapp.features.explore.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentExploreBinding
import com.example.mymovieapp.features.explore.ui.adapter.ExploreMovieFilterAdapter
import com.example.mymovieapp.features.explore.ui.dialog.ExploreMovieFilterDialog
import com.example.mymovieapp.features.explore.ui.dialog.ExploreMovieFilterDialog.Companion.FRAGMENT_RESULT_MOVIE_FILTER_ITEM
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterItem
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterUtils
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>(R.layout.fragment_explore) {

    private val viewModel : ExploreViewModel by viewModels()
    private val args : ExploreFragmentArgs by navArgs()

    @Inject
    lateinit var filterUtils: MovieFilterUtils

    private lateinit var  filterItemListAdapter : ExploreMovieFilterAdapter
    private lateinit var movieFilterItem : MovieFilterItem
    private lateinit var genreList : Map<Int,String>

    private var textChangingListenerJob : Job? = null

    private val exploreMoviesClickListener = object : MyClickListeners<Int> {
        override fun click(item: Int) {
            viewModel.setSearchStringInput(null)
            val action = ExploreFragmentDirections.actionExploreFragmentToMovieDetailFragment().setMovieId(item)
            findNavController().navigate(action)
        }

    }

    private var firstOpeningPagingData : PagingData<Movie>? = null
    private var discoveredPagingData : PagingData<Movie>? = null


    private val pagingAdapter : ExploreMoviePagingAdapter by lazy {
        ExploreMoviePagingAdapter(clickListeners = exploreMoviesClickListener)
    }

    private lateinit var categoryType : CategoryType

    private var searchString : String? = null

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        setBaseViewModel(viewModel)
        categoryType = args.categoryType
        viewModel.getGenreList("en")

        setFragmentResultListener(FRAGMENT_RESULT_LISTENER_KEY){ requestKey, bundle ->
            val result : MovieFilterItem? = bundle.getParcelable(FRAGMENT_RESULT_MOVIE_FILTER_ITEM)
            viewModel.setMovieFilterItem(result)
        }

        binding.exploreFragmentTitle.text = String.format(resources.getString(R.string.explore_title),resources.getString(categoryType.categoryName))

    }
    override fun setUpUI() {
        handleOverlaps()
        setUpRecyclerView()
    }

    override fun setUpObservers() {
        lifecycleScope.launch(SupervisorJob()) {
            launch {
                viewModel.getMoviesList(categoryType,"en").collectLatest {
                    firstOpeningPagingData = it
                    pagingAdapter.submitData(it)
                }
            }
            launch {
                viewModel.movieGenreList.collectLatest {
                    Timber.tag(TAG).d("Genre List : $it")
                    genreList = it
                }
            }
            launch {
                viewModel.savedMovieFilterItem.collectLatest { movieFilterItem ->
                    this@ExploreFragment.movieFilterItem = movieFilterItem
                    val adapterList = mutableListOf<MovieFilterDialogItem>()
                    if (movieFilterItem == filterUtils.getInitialMovieFilterItem()) {
                        binding.filterItemRecyclerView.toGone()
                    } else {
                        binding.filterItemRecyclerView.toVisible()
                    }
                    movieFilterItem.genresFilterItem.forEach {
                        adapterList.add(it)
                    }
                    adapterList.add(movieFilterItem.regionFilterItem)
                    adapterList.add(movieFilterItem.timeFilterItem)
                    movieFilterItem.sortFilterItem?.let {
                        adapterList.add(it)
                    }
                    filterItemListAdapter.submitList(adapterList)
                }
            }
            launch {
                viewModel.getSearchingMovieList("en").collectLatest {
                    it?.let {
                        pagingAdapter.submitData(it)
                    }
                }
            }
            launch {
                viewModel.getDiscoveryMovieList("en").collectLatest {
                    it?.let {
                        discoveredPagingData = it
                        pagingAdapter.submitData(it)
                    }
                }
            }
            launch {
                pagingAdapter.loadStateFlow.collectLatest {
                    when (it.refresh){
                        is LoadState.Loading -> {
                            viewModel.showLoading.value = true
                            binding.exploreMoviesRecyclerview.toInvisible()
                            binding.notFoundView.root.toGone()
                        }
                        is LoadState.NotLoading -> {
                            delay(1000L)
                            if (pagingAdapter.itemCount < 1) {
                                viewModel.showLoading.value = false
                                binding.exploreMoviesRecyclerview.toInvisible()
                                binding.notFoundView.root.toVisible()
                                binding.notFoundView.notFoundText.text = stringProvider.getString(R.string.your_search_not_found)
                                binding.notFoundView.notFoundImage.setImageResource(R.drawable.your_search_not_found_image)
                            } else {
                                viewModel.showLoading.value = false
                                binding.exploreMoviesRecyclerview.toVisible()
                                binding.exploreMoviesRecyclerview.smoothScrollToPosition(0)
                                binding.notFoundView.root.toGone()
                            }
                        }
                        is LoadState.Error -> {
                            delay(1000L)
                            viewModel.showLoading.value = false
                            binding.exploreMoviesRecyclerview.toInvisible()
                            binding.notFoundView.root.toVisible()
                            binding.notFoundView.notFoundText.text = (it.refresh as LoadState.Error).error.message
                        }
                    }
                }
            }
        }
    }

    override fun setUpListeners() {
        binding.apply {
            searchTextInputField.doAfterTextChanged {
                if (it.isNullOrEmpty()){
                    Timber.tag(TAG).d("inputFieldDoAfterTextChange Triggered")
                    textChangingListenerJob?.cancel()
                    searchTextInputField.clearFocus()
                    deleteTextButton.toGone()
                    lifecycleScope.launch {
                        if (movieFilterItem == filterUtils.getInitialMovieFilterItem()) {
                            firstOpeningPagingData?.let { pagingData ->  pagingAdapter.submitData(pagingData) }
                        } else {
                            discoveredPagingData?.let { pagingData ->  pagingAdapter.submitData(pagingData) }
                        }
                    }
                    root.hideKeyboard()
                } else {
                    textChangingListenerJob?.cancel()
                    textChangingListenerJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(500L)
                        val searchString = it.toString()
                        Timber.tag(TAG).d(searchString)
                        viewModel.setSearchStringInput(searchString)
                    }
                    deleteTextButton.toVisible()
                }
            }
            searchTextInputField.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus || !searchString.isNullOrEmpty()) {
                    searchBarContainer.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.primary_color)))
                    searchBarContainer.setCardBackgroundColor(ColorStateList.valueOf(resources.getColor(R.color.primary_color_soft)))
                } else {
                    searchBarContainer.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.warmGrey)))
                    searchBarContainer.setCardBackgroundColor(resources.getColor(R.color.white_three))
                }
            }
            deleteTextButton.setOnClickListener {
                searchString = ""
                searchTextInputField.text?.clear()
                searchTextInputField.clearFocus()
                searchBarContainer.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.warmGrey)))
                searchBarContainer.setCardBackgroundColor(resources.getColor(R.color.white_three))
                lifecycleScope.launch { firstOpeningPagingData?.let { pagingData ->  pagingAdapter.submitData(pagingData) } }
                it.hideKeyboard()
            }

            exploreFilterButton.setOnClickListener {
                val statsFilterBottomSheetFragment = ExploreMovieFilterDialog.newInstance(
                    movieFilterItem = movieFilterItem,
                    requestKey = FRAGMENT_RESULT_LISTENER_KEY,
                    genreList = genreList
                )
                statsFilterBottomSheetFragment.show(parentFragmentManager,"")
            }
            movieExploreBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun setUpRecyclerView(){
        binding.exploreMoviesRecyclerview.layoutManager = GridLayoutManager(this.context,2,RecyclerView.VERTICAL,false)
        binding.exploreMoviesRecyclerview.adapter = pagingAdapter
        binding.exploreMoviesRecyclerview.addItemDecoration(
            EqualSpacingItemDecoration(
                12.dp,
                EqualSpacingItemDecoration.GRID
            )
        )
        filterItemListAdapter = ExploreMovieFilterAdapter(object : MyClickListeners<MovieFilterDialogItem> {
            override fun click(item: MovieFilterDialogItem) {
                return
            }
        })
        binding.filterItemRecyclerView.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL,false)
        binding.filterItemRecyclerView.adapter = filterItemListAdapter
        binding.filterItemRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                2.dp,
                EqualSpacingItemDecoration.HORIZONTAL
            )
        )
    }

    override fun onDestroy() {
        firstOpeningPagingData = null
        super.onDestroy()
    }



    private fun handleOverlaps(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams>{
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    companion object {
        private val TAG = ExploreFragment::class.java.simpleName
        const val FRAGMENT_RESULT_LISTENER_KEY = "fragmentResultListenerKey"
    }
}