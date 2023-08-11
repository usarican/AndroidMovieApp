package com.example.mymovieapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.mymovieapp.R
import com.example.mymovieapp.features.home.ui.HomeFragment
import timber.log.Timber
import javax.inject.Inject

class FragmentUtils @Inject constructor(){

    fun refreshFragment(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            val navHostFragment = (fragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment)
            val navController = navHostFragment.navController
            val currentDestinationId = navController.currentDestination?.id
            currentDestinationId?.let {
                navController.navigate(it)
            }
        }
    }

}