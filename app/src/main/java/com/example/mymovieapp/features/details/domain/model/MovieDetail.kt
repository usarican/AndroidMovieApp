package com.example.mymovieapp.features.details.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    val id : Int,
    val movieTitle : String,
    val content : String,
    val movieTime : String,
    val image: String?,
    val releaseDate: String?,
    val genres: List<String>,
    val voteCount: Int,
    val movieScore: Double,
    val status : String,
    val backImage : String?
) : Parcelable
