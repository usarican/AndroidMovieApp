package com.example.mymovieapp.features.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: AuthenticationViewModel by activityViewModels()

    // TODO: Bunu Firebase'de parametre olarak tut
    private var userEnteringTheAppIsFirstTime: Boolean = true

    override fun setUpListeners() {
        binding.apply {
            loginButtonSignIn.setOnClickListener {
                userLogIn()
            }
        }
    }

    override fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.getAuthUserDataStateFlow().collectLatest {
                binding.textinputEmail.editText?.setText(it.userMail)
            }
        }
    }

    private fun userLogIn() {
        if (userEnteringTheAppIsFirstTime) {
            val action = LoginFragmentDirections.actionLoginFragmentToSetupProfileFragment()
            findNavController().navigate(action)
        } else {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
            findNavController().navigate(action)
        }
    }
}