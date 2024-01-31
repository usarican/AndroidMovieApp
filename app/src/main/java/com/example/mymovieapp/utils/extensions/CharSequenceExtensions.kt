package com.example.mymovieapp.utils.extensions

import java.util.regex.Pattern

fun CharSequence?.hasLowercase() = Pattern.matches("(.*[a-z].*)", this?.toString() ?: "")
fun CharSequence?.hasUppercase() = Pattern.matches("(.*[A-Z].*)", this?.toString() ?: "")
fun CharSequence?.hasDigit() = Pattern.matches("(.*\\d.*)", this?.toString() ?: "")
fun CharSequence?.hasWhitespace() = this.toString().contains(" ")