package com.example.mymovieapp.utils.extensions

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.getCompatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getCompatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.darkModeEnabled(): Boolean =
    (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) == Configuration.UI_MODE_NIGHT_YES

fun Context.isFragmentAddedOnActivity() : Boolean {
    return if (this is Fragment) this.isAdded else false
}