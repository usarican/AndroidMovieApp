package com.example.mymovieapp.utils.extensions

import android.app.Service
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
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

fun View.toVisible() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

fun View.toInvisible() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}

fun View.toGone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

fun View.hideKeyboard() {
    (context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}