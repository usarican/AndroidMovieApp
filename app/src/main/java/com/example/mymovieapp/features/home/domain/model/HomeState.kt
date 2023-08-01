package com.example.mymovieapp.features.home.domain.model

import com.example.mymovieapp.core.data.State

data class HomeUIState(
    var trendingMoviesState: UserInterfaceState,
    var categoryMoviesState: UserInterfaceState
)
data class CategoryMoviesUIState(
    var popularMoviesState: UserInterfaceState,
    var upComingMoviesState: UserInterfaceState,
    var topRatedMoviesState: UserInterfaceState
) {
    fun categoryMoviesIsDisplay () : Boolean {
        return popularMoviesState is UserInterfaceState.DisplayUI &&
                upComingMoviesState is UserInterfaceState.DisplayUI &&
                topRatedMoviesState is UserInterfaceState.DisplayUI
    }
}

sealed class UserInterfaceState {
    object DisplayLoading : UserInterfaceState()
    object DisplayUI : UserInterfaceState()
    class DisplayError (val error : Throwable) : UserInterfaceState()
}

