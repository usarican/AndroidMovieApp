package com.example.mymovieapp.utils.extensions

import android.content.res.Resources
import android.util.TypedValue

fun Int.toDp(resources: Resources): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics)
}