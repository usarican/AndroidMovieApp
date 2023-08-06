package com.example.mymovieapp.utils.extensions

import android.view.ViewStub

fun ViewStub.inflate(inflate : Boolean){
    if (inflate){
        this.inflate()
    }
}