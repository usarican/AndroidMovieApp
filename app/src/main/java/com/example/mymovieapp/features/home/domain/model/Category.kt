package com.example.mymovieapp.features.home.domain.model

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.parcelize.Parcelize

data class CategoryList(
    val categoryList : List<Category>
)
data class Category(
    val categoryType : CategoryType,
    val categoryName : String,
    val categoryItems : PagingData<Movie>?,
    //val seeAllButtonClickListener : () -> Unit
)

@Parcelize
enum class CategoryType(val id : Int) : Parcelable {
    POPULAR(0),
    TOP_RATED(1),
    UP_COMING(2)
}
