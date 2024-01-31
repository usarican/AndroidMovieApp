package com.example.mymovieapp.utils.validatorHelper

import android.util.Patterns
import com.example.mymovieapp.R

class EmailValidator : BaseValidator() {

    override var input: String? = null
    override fun validate(): ValidateResult {
        val isValid = input.isValidEmail()
        return ValidateResult(
            isValid,
            if (!isValid) R.string.email_validation_text else null
        )
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}