package com.example.mymovieapp.utils

interface BannerMovieItemClickListener {
    fun clickBannerMovie(movieId: Int)
}

interface CategoryMovieItemClickListeners {
    fun clickCategoryItem(movieId: Int)
}

interface MyClickListeners<T : Any> {
    fun click(item : T)
}