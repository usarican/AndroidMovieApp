package com.example.mymovieapp.utils.extensions

import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.BindingAdapter

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
@BindingAdapter("isVisible")
fun View.isVisible(isVisible : Boolean){
    if (isVisible.isNull()){
        return
    } else {
        visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}