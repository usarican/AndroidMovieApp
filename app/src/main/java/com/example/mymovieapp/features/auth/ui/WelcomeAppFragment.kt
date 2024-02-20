package com.example.mymovieapp.features.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentWelcomeAppBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeAppFragment : BaseFragment<FragmentWelcomeAppBinding>(R.layout.fragment_welcome_app) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            googleSignInClient.revokeAccess()
        }
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