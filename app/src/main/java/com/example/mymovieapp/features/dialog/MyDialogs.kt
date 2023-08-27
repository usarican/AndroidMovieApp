package com.example.mymovieapp.features.dialog

import com.example.mymovieapp.core.ui.MyDialog
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.utils.MyClickListeners

class BannerMovieDetailDialog(
    val movieDetailItem: MovieDetail,
    val clickListeners: MyClickListeners<Int>
) : MyDialog()