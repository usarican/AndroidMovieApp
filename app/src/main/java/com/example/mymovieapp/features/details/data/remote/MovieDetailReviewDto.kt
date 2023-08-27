package com.example.mymovieapp.features.details.data.remote

import com.squareup.moshi.Json

data class MovieDetailReviewDto(
    val author : String,
    val author_details : ReviewAuthorDetail,
    val content : String,
    @Json(name = "created_at") val createDate : String
) {
    data class ReviewAuthorDetail(
        @Json(name = "username") val authorUserName : String,
        @Json(name = "avatar_path") val authorProfilePicture : String?,
        @Json(name = "rating") val authorRate : Int?
    )
}
