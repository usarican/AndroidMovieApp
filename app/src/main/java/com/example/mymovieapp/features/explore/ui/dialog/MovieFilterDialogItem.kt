package com.example.mymovieapp.features.explore.ui.dialog

data class MovieFilterDialogItem(
    val itemCategory : FilterDialogItemCategory,
    val id : Int,
    val itemName : String,
    val itemCode : Any?,
    val isItemSelected : Boolean
)

enum class FilterDialogItemCategory{
    REGIONS,GENRE,TIME,SORT
}
