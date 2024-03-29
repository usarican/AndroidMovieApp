package com.example.mymovieapp.features.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.mymovieapp.core.ui.MyDialog

class DialogManager(
    private val fragmentManager: FragmentManager
) {

    fun showDialogFragment(dialog : MyDialog) {
        showDialog(dialog.create())
    }

    private fun showDialog(fragment: DialogFragment, customTag: String? = null) {
        val tag = customTag ?: fragment.javaClass.simpleName
        fragment.show(fragmentManager, tag)
    }
}