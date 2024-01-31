package com.example.mymovieapp.utils.validatorHelper

import androidx.annotation.StringRes

data class ValidateResult(
    val isSuccess : Boolean?,
    @StringRes val errorMessage : Int?
)
