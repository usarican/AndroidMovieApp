package com.example.mymovieapp.features.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentWelcomeAppBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeAppFragment : BaseFragment<FragmentWelcomeAppBinding>(R.layout.fragment_welcome_app) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("75915502075-ittsi2rmdijnijsi2dcd1lc1trn25ftg.apps.googleusercontent.com")
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
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