package com.example.mymovieapp.utils

import com.example.mymovieapp.utils.Constants.IMAGE_BASE_URL

object ImageApi {
    fun getImage(
        baseUrl: String = IMAGE_BASE_URL,
        imageSize: String = ImageSize.ORIGINAL.path,
        imageUrl: String?
    ) = "$baseUrl$imageSize$imageUrl"
}

enum class ImageSize(val path: String) {
    W500("w500"),
    W780("w780"),
    W45("w45"),
    W185("w185"),
    W300("w300"),
    W92("w92"),
    H632("h632"),
    ORIGINAL("original");

}