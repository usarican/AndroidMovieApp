package com.example.mymovieapp.core.ui

import android.app.Activity
import androidx.fragment.app.DialogFragment

interface MyUIComponents<T : Any> {
    fun create() : T
}

interface MyDialog : MyUIComponents<DialogFragment>

interface MyActivity : MyUIComponents<Activity>