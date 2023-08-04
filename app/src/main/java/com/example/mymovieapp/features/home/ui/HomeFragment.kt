package com.example.mymovieapp.features.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.databinding.FragmentHomeBinding
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import com.example.mymovieapp.features.home.ui.adapter.CategoryAdapter
import com.example.mymovieapp.utils.ViewPagerTransformer
import com.example.mymovieapp.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    private val viewModel : HomeViewModel by viewModels()

    private val bannerMoviesAdapter: BannerMoviesAdapter by lazy {
        BannerMoviesAdapter()
    }
    private val categoryAdapter : CategoryAdapter by lazy { CategoryAdapter(lifecycleScope) }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        viewModel.getTrendMoviesOfWeek()
        viewModel.getCategoryMoviesList()
        setUpViewPager()
        setUpRecyclerView()
    }

    override fun setUpObservers() {
        viewModel.getTrendingMoviesOfWeekLiveData().observe(viewLifecycleOwner){
            bannerMoviesAdapter.submitList(it)
            binding.bannerMovieIndicator.setViewPager2(binding.bannerMoviesViewPager)
        }

        lifecycleScope.launch {
            viewModel.viewPagerSavedState.collectLatest { savedPage ->
                binding.bannerMoviesViewPager.whenReady {
                    binding.bannerMoviesViewPager.setCurrentItem(savedPage,true)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getCategoryMoviesStateFlow().collectLatest { categoryListItem ->
                Timber.tag(TAG).d("category Item List added $categoryListItem")
                categoryAdapter.submitList(categoryListItem?.categoryList)
            }
        }
        lifecycleScope.launch {
            viewModel.getHomeState().collectLatest { state ->
                Timber.tag(TAG).d("HOME STATES ARE = $state")
                val layoutViewState = LayoutViewState(state)
                binding.layoutViewState = layoutViewState
                Timber.tag(TAG).d("Layout View State is Loading ${layoutViewState.isLoading()} is Success ${layoutViewState.isSuccess()}")
            }
        }
    }

    private fun setUpViewPager(){
        binding.bannerMoviesViewPager.apply {
            adapter = bannerMoviesAdapter
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
        categoryAdapter.setPagingLoadStateCallBack(viewModel.pagingLoadStateCallBack)
        binding.categoryRecyclerView.adapter = categoryAdapter
        Timber.tag(TAG).d("PagingLoadState is = ${viewModel.pagingLoadStateCallBack}")
    }


    companion object {
        private val TAG = HomeFragment::class.java.name
    }
}