package com.example.mymovieapp.features.details.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.core.ui.BasePageAdapter
import com.example.mymovieapp.core.ui.BasePageAdapterForFragment
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.databinding.FragmentDetailBinding
import com.example.mymovieapp.features.details.ui.adapter.BannerMovieGenreListAdapter
import com.example.mymovieapp.features.details.ui.adapter.MovieDetailCastListAdapter
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.extensions.dp
import com.example.mymovieapp.utils.extensions.inflate
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel : MovieDetailViewModel by viewModels()
    private val args : MovieDetailFragmentArgs by navArgs()
    private val movieDetailCastListAdapter : MovieDetailCastListAdapter by lazy {
        MovieDetailCastListAdapter()
    }
    private val movieDetailGenreListAdapter : BannerMovieGenreListAdapter by lazy {
        BannerMovieGenreListAdapter()
    }
    private var movieId : Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val window = this.activity?.window
        window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        setBaseViewModel(viewModel)
        this.movieId = args.movieId
        viewModel.getMovieDetailInformation(movieId,"en")
        initRecyclerViews()
        initViewPager()
    }

    override fun onDetach() {
        val window = this.activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onDetach()
    }
    override fun setUpListeners() {
        binding.movieDetailBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setUpObservers() {
        viewModel.apply {
            getMovieDetailInformationLiveData().observe(viewLifecycleOwner) {
                binding.movieDetailItem = it
                Timber.tag("Movie Detail Fragment").d(it.toString())
            }
            lifecycleScope.launch {
                getMovieDetailLayoutViewStateFlow().collectLatest {
                    Timber.tag("Movie Detail Fragment").d("LayoutViewState = $it")
                    binding.layoutViewState = it
                }
            }
        }
    }

    private fun initRecyclerViews(){
        binding.apply {
            movieDetailGenreListRecyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            movieDetailGenreListRecyclerView.adapter = movieDetailGenreListAdapter
            movieDetailGenreListRecyclerView.addItemDecoration(
                EqualSpacingItemDecoration(
                    4.dp,
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            )
            movieDetailCastRecyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            movieDetailCastRecyclerView.adapter = movieDetailCastListAdapter
            movieDetailCastRecyclerView.addItemDecoration(
                EqualSpacingItemDecoration(
                    4.dp,
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            )
        }
    }

    private fun initViewPager(){
        val fragments : Array<Fragment> = arrayOf(
            MovieDetailCommentsFragment(),
            MovieDetailMoreLikeThisFragment(),
            MovieDetailTrailersFragment()
        )
        val fragmentsName : Array<String> = arrayOf(
            resources.getString(R.string.comments),
            resources.getString(R.string.more_like_this),
            resources.getString(R.string.trailers)
        )

        binding.detailPageViewPager.apply {
            isUserInputEnabled = false
            adapter = BasePageAdapterForFragment(this@MovieDetailFragment,fragments)
            offscreenPageLimit = fragments.size
        }

        TabLayoutMediator(binding.detailPageTabLayout,binding.detailPageViewPager){ tab, position ->
            tab.text = fragmentsName[position]

        }.attach()
    }

    private fun inflateLayoutError(layoutViewState: LayoutViewState) {
        binding.layoutError.viewStub?.inflate(layoutViewState.isError())
    }
}