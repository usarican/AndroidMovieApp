package com.example.mymovieapp.features.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentWelcomeAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeAppFragment : BaseFragment<FragmentWelcomeAppBinding>(R.layout.fragment_welcome_app) {
    private val viewModel : AuthenticationViewModel by viewModels()

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        viewModel.clearDatabaseTable()
    }
    override fun setUpListeners() {
        binding.apply {
            buttonSignUp.setOnClickListener {
                signUpTheApp()
            }
            buttonHaveAccount.setOnClickListener {
                logInTheApp()
            }
        }
    }

    private fun logInTheApp() {
        val action = WelcomeAppFragmentDirections.actionWelcomeAppFragmentToLoginFragment()
        findNavController().navigate(action)

    }

    private fun signUpTheApp() {
        val action = WelcomeAppFragmentDirections.actionWelcomeAppFragmentToSignUpFragment()
        findNavController().navigate(action)
    }
}