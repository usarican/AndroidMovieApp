package com.example.mymovieapp.features.splash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseActivity
import com.example.mymovieapp.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
open class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.splash_navigation_container) as NavHostFragment).navController
    }


    @Inject
    lateinit var auth: FirebaseAuth
    override fun bindingFactory(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }


    override fun setUpViews(savedInstanceState: Bundle?) {
        val navigationCallback = object : NavigationCallback {
            override fun navigateToHomePage() {
                val action = SplashFragmentDirections.actionSplashFragmentToMainActivity2()
                navController.navigate(action)
            }

        }

        viewModel.setNavigationCallback(navigationCallback)
        if (auth.currentUser != null) {
            viewModel.getUserInformation(auth.uid)
        } else {
            val action = SplashFragmentDirections.actionSplashFragmentToAuthenticationActivity()
            navController.navigate(action)
        }
    }

}