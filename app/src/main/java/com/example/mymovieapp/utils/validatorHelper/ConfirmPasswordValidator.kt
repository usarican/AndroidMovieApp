package com.example.mymovieapp.utils.validatorHelper

import com.example.mymovieapp.R

class ConfirmPasswordValidator(
    private val password : String?
) : BaseValidator() {
    override var input: String? = null


    override fun validate(): ValidateResult {
        val isValid = input == password
        return ValidateResult(
            isValid,
            if (!isValid) R.string.password_confirm_validation_text else null
        )
    }
}