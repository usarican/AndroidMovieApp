package com.example.mymovieapp.features.auth.ui


import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentSignUpBinding
import com.example.mymovieapp.utils.extensions.validate
import com.example.mymovieapp.utils.validatorHelper.ConfirmPasswordValidator
import com.example.mymovieapp.utils.validatorHelper.EmailValidator
import com.example.mymovieapp.utils.validatorHelper.EmptyValidator
import com.example.mymovieapp.utils.validatorHelper.PasswordValidator
import com.google.android.material.snackbar.Snackbar

class SignUpFragment :  BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val viewModel : AuthenticationViewModel by activityViewModels()
    override fun setUpListeners() {
        binding.apply {
            loginButtonSignUp.setOnClickListener {
                if (validateAll()){
                    viewModel.createNewUser(
                        email = textinputEmail.editText?.text.toString(),
                        password = textinputPassword.editText?.text.toString()
                    )
                    viewModel.setUserEmail(textinputEmail.editText?.text.toString())
                }
            }
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setUpObservers() {
        viewModel.navigateLoginPage().observe(viewLifecycleOwner){
            it?.let {
                if (it) {
                    viewModel.setNavigateLoginPageToFalse()
                    val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun validateAll() : Boolean {
        binding.apply {
            val validation1 = textinputEmail.validate(
                EmailValidator(),EmptyValidator()
            )
            val validation2 = textinputPassword.validate(
                PasswordValidator(),EmptyValidator()
            )
            val validation3 =textinputPasswordConfirm.validate(
                ConfirmPasswordValidator(textinputPassword.editText?.text.toString()),EmptyValidator()
            )
            return validation1 && validation2 && validation3
        }
    }


}