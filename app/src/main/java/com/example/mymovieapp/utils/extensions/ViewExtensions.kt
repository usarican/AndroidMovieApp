package com.example.mymovieapp.utils.extensions

import android.view.View
import android.view.ViewTreeObserver

fun View.whenReady(behavior: (() -> Unit)) {
    if (this.height != 0 && this.width != 0) {
        post { behavior.invoke() }
    } else {
        this.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                this@whenReady.viewTreeObserver.removeOnGlobalLayoutListener(this)
                post { behavior.invoke() }
            }
        })
    }
}