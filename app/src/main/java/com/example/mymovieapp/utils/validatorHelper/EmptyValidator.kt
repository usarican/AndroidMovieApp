package com.example.mymovieapp.utils.validatorHelper

import com.example.mymovieapp.R


class EmptyValidator : BaseValidator() {
    override var input: String? = null

    override fun validate(): ValidateResult {
        val isValid = input?.isNotEmpty()
        return ValidateResult(
            isValid,
            if(isValid != true) R.string.is_empty_validation_text else null
        )
    }
}