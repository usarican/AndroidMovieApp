package com.example.mymovieapp.utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

class StringProvider(private val context : Context) {
    fun getString(@StringRes stringResourceId : Int) : String = context.getString(stringResourceId)
    fun getStringArray(@ArrayRes stringResourceId : Int) : Array<String> = context.resources.getStringArray(stringResourceId)
}