package com.example.mymovieapp.features.explore.ui.dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MovieFilterDialogItem(
    val itemCategory : FilterDialogItemCategory,
    val id : Int,
    val itemName : String,
    val itemCode : @RawValue Any?,
    val isItemSelected : Boolean
) : Parcelable

enum class FilterDialogItemCategory{
    REGIONS,GENRE,TIME,SORT
}
@Parcelize
data class MovieFilterItem(
    val regionFilterItem : MovieFilterDialogItem,
    val genresFilterItem : List<MovieFilterDialogItem>,
    val timeFilterItem : MovieFilterDialogItem,
    val sortFilterItem : MovieFilterDialogItem? = null

) : Parcelable
