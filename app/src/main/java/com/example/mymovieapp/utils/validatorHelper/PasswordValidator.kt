package com.example.mymovieapp.utils.validatorHelper

import com.example.mymovieapp.R
import com.example.mymovieapp.utils.extensions.hasDigit
import com.example.mymovieapp.utils.extensions.hasLowercase
import com.example.mymovieapp.utils.extensions.hasUppercase
import com.example.mymovieapp.utils.extensions.hasWhitespace

class PasswordValidator : BaseValidator() {
    override var input: String? = null

    override fun validate(): ValidateResult {
        val isValidCondition1 = input.hasLowercase() && input.hasUppercase()
        val isValidCondition2 = !input.hasWhitespace() && (input?.length ?: 0) >= 8
        val isValidCondition3 = input.hasDigit()

        val isValid = isValidCondition1 && isValidCondition2 && isValidCondition3

        return ValidateResult(
            isValid,
            if (!isValid) R.string.password_validation_text else null
        )

    }
}