package com.example.mymovieapp.features.home.domain.model

data class HomeState(
    private val trendingMovieList : List<Movie>,
    private val popularMoviesList : Category,
    private val topRatedMoviesList : Category,
    private val upComingMovesList : Category
)
