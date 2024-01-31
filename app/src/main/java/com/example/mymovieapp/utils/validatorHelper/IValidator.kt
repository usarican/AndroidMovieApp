package com.example.mymovieapp.utils.validatorHelper

interface IValidator {
    var input : String?
    fun validate() : ValidateResult
}