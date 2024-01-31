package com.example.mymovieapp.features.auth.ui


import android.widget.Toast
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
    override fun setUpListeners() {
        binding.apply {
            loginButtonSignUp.setOnClickListener {
                val validation = validateAll()
                val toast =
                    if (!validation) Toast.makeText(context,"Validation Fail",Toast.LENGTH_LONG)
                    else Toast.makeText(context,"Validation Fail",Toast.LENGTH_LONG)
                toast.show()
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

    private fun clearTextInputsFocus() {
        val editTextFields = listOf(binding.textinputEmail.editText,binding.textinputPassword.editText,binding.textinputPasswordConfirm.editText)
        editTextFields.forEach { t ->
            t?.clearFocus()
        }
    }
}