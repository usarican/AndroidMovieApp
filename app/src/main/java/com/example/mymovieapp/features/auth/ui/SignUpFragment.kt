package com.example.mymovieapp.features.auth.ui


import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentSignUpBinding
import com.example.mymovieapp.utils.extensions.validate
import com.example.mymovieapp.utils.validatorHelper.EmptyValidator

class SignUpFragment :  BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun setUpListeners() {
        binding.textinputEmail.validate(
            EmptyValidator()
        )
    }
}