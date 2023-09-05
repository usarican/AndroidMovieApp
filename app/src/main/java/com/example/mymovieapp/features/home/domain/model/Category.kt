package com.example.mymovieapp.features.home.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.example.mymovieapp.R
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
enum class CategoryType(
    val id : Int,
    @StringRes val categoryName : Int
    ) : Parcelable {
    POPULAR(0, R.string.Category_Name_Popular),
    TOP_RATED(1, R.string.Category_Name_Top_Rated),
    UP_COMING(2,R.string.Category_Name_Up_Coming)
}
