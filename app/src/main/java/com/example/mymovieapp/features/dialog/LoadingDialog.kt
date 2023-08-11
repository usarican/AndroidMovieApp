package com.example.mymovieapp.features.dialog

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymovieapp.R

class LoadingDialog(
    private val context : Context
    ) {
    private val loadingDialog by lazy {
        Dialog(context).apply {
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
            setContentView(R.layout.layout_loading)
        }
    }

    fun showLoadingDialog(){
        if (context is Fragment) if (!(context.isAdded)) return
        try {
            loadingDialog.show()
        } catch (ignore: java.lang.Exception) {}
    }

    fun hideLoadingDialog(){
        if (context is Fragment) if (!(context.isAdded)) return
        try {
            loadingDialog.dismiss()
        } catch (ignore: java.lang.Exception) {}
    }
}