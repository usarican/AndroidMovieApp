package com.example.mymovieapp.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.widget.doAfterTextChanged
import com.example.mymovieapp.utils.validatorHelper.BaseValidator
import com.example.mymovieapp.utils.validatorHelper.IValidator
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.validate(vararg validators : IValidator) : Boolean {
    this.setErrorTextColor(ColorStateList.valueOf(Color.RED))
    this.boxStrokeErrorColor = (ColorStateList.valueOf(Color.RED))
    this.editText?.text.let { input ->
        validators.forEach { it.input = input.toString() }
        val validateResult = BaseValidator.validate(validators.toList())
        if (validateResult.isSuccess != true){
            this.error = validateResult.errorMessage?.let { this.context.getString(it) }
            return false
        } else {
            this.error = null
            return true
        }
    }
}