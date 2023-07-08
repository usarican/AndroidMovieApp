package com.example.mymovieapp.features.home.ui

import androidx.fragment.app.viewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentHomeBinding
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
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
        binding.bannerMoviesViewPager.adapter = bannerMoviesAdapter
    }

    companion object {
        private val TAG = HomeFragment::class.java.name
    }
}