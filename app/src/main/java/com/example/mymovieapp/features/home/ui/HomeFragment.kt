package com.example.mymovieapp.features.home.ui

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentHomeBinding
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import com.example.mymovieapp.utils.ViewPagerTransformer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    private val viewModel : HomeViewModel by viewModels()
    private lateinit var bannerMoviesAdapter: BannerMoviesAdapter
    override fun setUpUI() {
        viewModel.getTrendMoviesOfWeek()
        setUpViewPager()
    }

    override fun setUpObservers() {
        viewModel.getTrendingMoviesOfWeekLiveData().observe(viewLifecycleOwner){
            bannerMoviesAdapter.submitList(it)
            Timber.tag(TAG).d(it.toString())
        }
    }

    private fun setUpViewPager(){
        bannerMoviesAdapter = BannerMoviesAdapter()
        binding.bannerMoviesViewPager.apply {
            adapter = bannerMoviesAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePagerTransformer = CompositePageTransformer()
            compositePagerTransformer.addTransformer(MarginPageTransformer(40))
            compositePagerTransformer.addTransformer(ViewPagerTransformer())
            setPageTransformer(compositePagerTransformer)
        }

    }

    companion object {
        private val TAG = HomeFragment::class.java.name
    }
}