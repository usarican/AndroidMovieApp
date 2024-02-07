package com.example.mymovieapp.features.auth.ui


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.core.ui.BasePageAdapterForFragment
import com.example.mymovieapp.databinding.FragmentSetupProfileBinding
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>(R.layout.fragment_setup_profile) {

    val viewModel: AuthenticationViewModel by activityViewModels()
    override fun setUpUI() {
        initViewPager()
        handleToolbarOverlaps(binding.exploreToolbar)
    }
    private var nextPageIsAvailable = true


    private val pageTitle : Array<String> = arrayOf(
        "Choose Your Interest",
        "Fill Your Profile"
    )
    override fun setUpListeners() {
        binding.setupViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.setupFragmentTitle.text = pageTitle[position]
            }
        })
        binding.backButton.setOnClickListener {
            if (viewModel.viewPagerCurrentPage.value == 1) {
                viewModel.viewPagerCurrentPage.compareAndSet(1,0)
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewPager() {
        val fragments : Array<Fragment> = arrayOf(
            SetupProfileSection1Fragment(),
            SetupProfileSection2Fragment()
        )

        binding.setupViewPager.apply {
            isUserInputEnabled = false
            adapter =  BasePageAdapterForFragment(this@SetupProfileFragment, fragments)
            offscreenPageLimit = fragments.size
        }
    }

    override fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.viewPagerCurrentPage.collectLatest {
                binding.setupViewPager.setCurrentItem(it,true)
            }
        }
    }
}