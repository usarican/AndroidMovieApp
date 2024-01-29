package com.example.mymovieapp.features.auth.ui


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.core.ui.BasePageAdapterForFragment
import com.example.mymovieapp.databinding.FragmentSetupProfileBinding
import com.example.mymovieapp.features.home.ui.adapter.BannerMoviesAdapter
import timber.log.Timber


class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>(R.layout.fragment_setup_profile) {

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
                nextPageIsAvailable = position != 1
            }
        })
        binding.applyButton.setOnClickListener {
            if (nextPageIsAvailable) {
                binding.setupViewPager.setCurrentItem(1,true)
            } else {
                val action = SetupProfileFragmentDirections.actionSetupProfileFragmentToMainActivity()
                findNavController().navigate(action)
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
}