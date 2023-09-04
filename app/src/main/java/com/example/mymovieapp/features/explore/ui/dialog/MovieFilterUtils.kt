package com.example.mymovieapp.features.explore.ui.dialog

import com.example.mymovieapp.R
import com.example.mymovieapp.utils.StringProvider
import java.util.Calendar


class MovieFilterUtils constructor(
    private val stringProvider: StringProvider
) {

    private val movieFilterRegionItemKeyList = listOf(
        "tr","us","az","cn","kr","de","jp","ru"
    )
    private val movieFilterSortItemKeyList = listOf(
        "popularity.desc","primary_release_date.desc","vote_average.desc"
    )
    fun getMovieFilterRegionItems(movieFilterItem: MovieFilterItem) : List<MovieFilterDialogItem> {
        val movieFilterRegionItemList = mutableListOf<MovieFilterDialogItem>()
        val initialItem =
            MovieFilterDialogItem(
                itemCategory =FilterDialogItemCategory.REGIONS,
                id = 0,
                itemCode = null,
                itemName = stringProvider.getString(R.string.all_regions),
                isItemSelected = true
            )
        movieFilterRegionItemList.add(initialItem)
        stringProvider.getStringArray(R.array.movieFilterRegionList).forEachIndexed { index, s ->
            val movieFilterRegionItem =
                MovieFilterDialogItem(
                    itemCategory =FilterDialogItemCategory.REGIONS,
                    id = index + 1,
                    itemCode = movieFilterRegionItemKeyList[index],
                    itemName = s,
                    isItemSelected = false
                )
            movieFilterRegionItemList.add(movieFilterRegionItem)
        }
        return movieFilterRegionItemList.map { currentListItem ->
            if (currentListItem.id == movieFilterItem.regionFilterItem.id){
                currentListItem.copy(
                    isItemSelected = true
                )
            } else currentListItem.copy(
                isItemSelected = false
            )
        }
    }

    fun getMovieFilterSortItems(movieFilterItem: MovieFilterItem) : List<MovieFilterDialogItem> {
        val movieFilterSortItemList = mutableListOf<MovieFilterDialogItem>()
        stringProvider.getStringArray(R.array.movieFilterSortList).forEachIndexed { index, s ->
            val movieFilterRegionItem =
                MovieFilterDialogItem(
                    itemCategory =FilterDialogItemCategory.SORT,
                    id = index + 1,
                    itemCode = movieFilterSortItemKeyList[index],
                    itemName = s,
                    isItemSelected = false
                )
            movieFilterSortItemList.add(movieFilterRegionItem)
        }
        return movieFilterSortItemList.map { currentListItem ->
            if (currentListItem.id == movieFilterItem.sortFilterItem?.id){
                currentListItem.copy(
                    isItemSelected = true
                )
            } else currentListItem.copy(
                isItemSelected = false
            )
        }
    }

    fun getMovieFilterTimeItems(movieFilterItem: MovieFilterItem) : List<MovieFilterDialogItem> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val movieFilterPeriodItemList = mutableListOf<MovieFilterDialogItem>()
        val initialItem =
            MovieFilterDialogItem(
                itemCategory =FilterDialogItemCategory.TIME,
                id = 0,
                itemCode = null,
                itemName = stringProvider.getString(R.string.all_periods),
                isItemSelected = true
            )
        movieFilterPeriodItemList.add(initialItem)
        var j = 1
        for (i in currentYear downTo 1980){
            val movieFilterPeriodItem =
                MovieFilterDialogItem(
                    itemCategory =FilterDialogItemCategory.TIME,
                    id = j,
                    itemCode = i,
                    itemName = i.toString(),
                    isItemSelected = false
                )
            movieFilterPeriodItemList.add(movieFilterPeriodItem)
            j++
        }
        return movieFilterPeriodItemList.map { currentListItem ->
            if (currentListItem.id == movieFilterItem.timeFilterItem.id){
                currentListItem.copy(
                    isItemSelected = true
                )
            } else currentListItem.copy(
                isItemSelected = false
            )
        }
    }

    fun getInitialMovieFilterItem() = MovieFilterItem(
        regionFilterItem = MovieFilterDialogItem(
            itemCategory =FilterDialogItemCategory.REGIONS,
            id = 0,
            itemCode = null,
            itemName = stringProvider.getString(R.string.all_regions),
            isItemSelected = true
        ),
        genresFilterItem = listOf(
            MovieFilterDialogItem(
                itemCategory = FilterDialogItemCategory.GENRE,
                id = 0,
                itemCode = null,
                itemName = stringProvider.getString(R.string.all_genres),
                isItemSelected = true
            )
        ),
        timeFilterItem = MovieFilterDialogItem(
            itemCategory =FilterDialogItemCategory.TIME,
            id = 0,
            itemCode = null,
            itemName = stringProvider.getString(R.string.all_periods),
            isItemSelected = true
        )
    )

}