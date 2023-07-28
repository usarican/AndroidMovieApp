package com.example.mymovieapp.features.home.domain.model

import androidx.paging.PagingData

data class CategoryList(
    val categoryList : List<Category>
)
data class Category(
    val categoryType : CategoryType,
    val categoryName : String,
    val categoryItems : PagingData<Movie>?,
    //val seeAllButtonClickListener : () -> Unit
)

enum class CategoryType(val id : Int){
    POPULAR(0),
    TOP_RATED(1),
    UP_COMING(2)
}
