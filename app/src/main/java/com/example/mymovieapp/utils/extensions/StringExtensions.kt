package com.example.mymovieapp.utils.extensions

fun String.releaseDateToYear() : String {
    return this.split("-")[0]
}