package com.example.mymovieapp.utils.extensions

fun List<Int?>.separateIntValueWithCommaAndReturnString() : String {
    var string = ""
    this.forEachIndexed { index, i ->
        string += if (index < this.size - 1) {
            ("$i,")
        } else {
            i.toString()
        }
    }
    return string
}