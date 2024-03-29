package com.example.mymovieapp.features.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.databinding.FragmentHomeBinding
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import com.example.mymovieapp.features.home.ui.adapter.CategoryAdapter
import com.example.mymovieapp.utils.CategoryMovieItemClickListeners
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.ViewPagerTransformer
import com.example.mymovieapp.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    private val viewModel : HomeViewModel by viewModels()

    private val categoryMovieItemClickListeners = object : CategoryMovieItemClickListeners {
        override fun clickCategoryItem(movieId: Int) {
            val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment().setMovieId(movieId)
            findNavController().navigate(action)
        }
    }
    private val seeAllButtonClickListener = object : MyClickListeners<CategoryType> {
        override fun click(item: CategoryType) {
            val action = HomeFragmentDirections.actionHomeFragmentToExploreFragment(item)
            findNavController().navigate(action)
        }

    }

    private val bannerMoviesAdapter: BannerMoviesAdapter by lazy {
        BannerMoviesAdapter()
    }
    private val categoryAdapter : CategoryAdapter by lazy { CategoryAdapter(lifecycleScope,categoryMovieItemClickListeners,seeAllButtonClickListener) }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        setBaseViewModel(viewModel)
        viewModel.setUIStateLoading()
        viewModel.getTrendMoviesOfWeek()
        viewModel.getCategoryMoviesList()
        handleToolbarOverlaps(binding.bannerMoviesViewPager)
        setUpViewPager()
        setUpRecyclerView()
    }

    override fun setUpObservers() {

        viewModel.getTrendingMoviesOfWeekLiveData().observe(viewLifecycleOwner){
            bannerMoviesAdapter.submitList(it)
            binding.bannerMovieIndicator.setViewPager2(binding.bannerMoviesViewPager)
        }

        viewModel.getRefreshFragmentLiveData().observe(viewLifecycleOwner){
            if (it){
                viewModel.setRefreshFragmentLiveData(false)
                viewModel.refreshFragment(activity?.supportFragmentManager)
            }
        }

        lifecycleScope.launch(SupervisorJob()) {
            launch {
                viewModel.viewPagerSavedState.collectLatest { savedPage ->
                    binding.bannerMoviesViewPager.whenReady {
                        binding.bannerMoviesViewPager.setCurrentItem(savedPage,true)
                    }
                }
            }
            launch {
                viewModel.getCategoryMoviesStateFlow().collectLatest { categoryListItem ->
                    Timber.tag(TAG).d("category Item List added $categoryListItem")
                    categoryAdapter.submitList(categoryListItem?.categoryList)
                }
            }
            launch {
                viewModel.getHomeState().collectLatest { layoutViewState ->
                    Timber.tag(TAG).d("HOME STATES SUCCESS = ${layoutViewState.isSuccess()} - ERROR = ${layoutViewState.isError()} - LOADING =  ${layoutViewState.isLoading()}")
                    binding.layoutViewState = layoutViewState
                    binding.executePendingBindings()
                    inflateLayoutError(layoutViewState)
                    delay(5000L)
                    viewModel.loadingTimeOut(layoutViewState)
                }
            }
        }
    }

    private fun setUpViewPager(){
        binding.bannerMoviesViewPager.apply {
            adapter = bannerMoviesAdapter
            bannerMoviesAdapter.setBannerMovieItemClickListener(
                viewModel.bannerMovieItemClickListener
            )
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setViewPagerSavedState(position)
                }
            })

            val compositePagerTransformer = CompositePageTransformer()
            compositePagerTransformer.addTransformer(MarginPageTransformer(40))
            compositePagerTransformer.addTransformer(ViewPagerTransformer())
            setPageTransformer(compositePagerTransformer)
        }
    }

    private fun setUpRecyclerView(){
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
        binding.categoryRecyclerView.adapter = categoryAdapter
        categoryAdapter.setPagingLoadStateCallBack(viewModel.pagingLoadStateCallBack)
        binding.categoryRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                16.dp,
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        Timber.tag(TAG).d("PagingLoadState is = ${viewModel.pagingLoadStateCallBack}")
    }

    override fun setUpListeners() {
        super.setUpListeners()
        val bannerMovieMoreDetailButtonClickListener = object : MyClickListeners<Int> {
            override fun click(item: Int) {
                val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment().setMovieId(item)
                findNavController().navigate(action)
            }
        }

        viewModel.setClickListener(bannerMovieMoreDetailButtonClickListener)
    }

    private fun inflateLayoutError(layoutViewState: LayoutViewState) {
        binding.layoutError.viewStub?.inflate(layoutViewState.isError())
    }

    companion object {
        private val TAG = HomeFragment::class.java.name
    }
}