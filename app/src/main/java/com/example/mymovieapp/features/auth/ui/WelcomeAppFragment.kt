package com.example.mymovieapp.features.auth.ui

import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentWelcomeAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeAppFragment : BaseFragment<FragmentWelcomeAppBinding>(R.layout.fragment_welcome_app) {

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