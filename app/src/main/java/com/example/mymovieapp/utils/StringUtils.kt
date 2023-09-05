package com.example.mymovieapp.utils

import androidx.annotation.ArrayRes

object StringUtils {

    fun parseStringArrayToMap(stringArray : Array<String>) : List<Pair<String,String>> {
        return stringArray.map {
            val parseStringItem = it.split("|")
            parseStringItem[0] to parseStringItem[1]
        }
    }
}