package com.example.mymovieapp.utils.validatorHelper

abstract class BaseValidator : IValidator {
    companion object {
        fun validate(validators : List<IValidator>): ValidateResult {
            var isSuccess = true
            var message : Int? = null
            validators.forEach { validator ->
                val validateResult = validator.validate()
                if (validateResult.isSuccess != true){
                    isSuccess = false
                    message = validateResult.errorMessage
                }
            }
            return ValidateResult(isSuccess,message)
        }
    }
}