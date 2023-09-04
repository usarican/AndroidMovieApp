package com.example.mymovieapp.features.explore.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.StringProvider
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreMovieFilterDialogViewModel @Inject constructor(
    private val genreListUseCase: GetGenreListUseCase,
    private val stringProvider: StringProvider,
    private val movieFilterUtils: MovieFilterUtils,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _movieFilterGenreListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())


    private val _movieFilterItemMutableLiveData = MutableLiveData(movieFilterUtils.getInitialMovieFilterItem())
    fun getMovieFilterItem() : LiveData<MovieFilterItem> = _movieFilterItemMutableLiveData


    fun setSavedMovieFilterItem(movieFilterItem: MovieFilterItem) {
        Timber.tag(TAG).d("New movie Filter Set : $movieFilterItem")
        _movieFilterItemMutableLiveData.value = movieFilterItem
    }
    fun getMovieFilterGenreItemList(): StateFlow<List<MovieFilterDialogItem>> =
        _movieFilterGenreListMutableStateFlow.asStateFlow()

    private val _movieFilterRegionListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())

    fun getMovieFilterRegionItemList(): StateFlow<List<MovieFilterDialogItem>> =
        _movieFilterRegionListMutableStateFlow.asStateFlow()

    private val _movieFilterPeriodListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())

    fun getMovieFilterPeriodItemList(): StateFlow<List<MovieFilterDialogItem>> =
        _movieFilterPeriodListMutableStateFlow.asStateFlow()

    private val _movieFilterSortListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())

    fun getMovieFilterSortItemList(): StateFlow<List<MovieFilterDialogItem>> =
        _movieFilterSortListMutableStateFlow.asStateFlow()

    private val _isEnabledMovieFilterApplyButton = MutableStateFlow(false)
    fun getMovieFilterButtonState(): StateFlow<Boolean> =
        _isEnabledMovieFilterApplyButton.asStateFlow()



    fun getGenreList(language: String,movieFilterItem: MovieFilterItem) {
            genreListUseCase.invoke(language).doOnSuccess { genreListMap ->
                val genreItemList = mutableListOf<MovieFilterDialogItem>()
                val initialGenreListItem =
                    MovieFilterDialogItem(
                        itemCategory = FilterDialogItemCategory.GENRE,
                        id = 0,
                        itemCode = null,
                        itemName = stringProvider.getString(R.string.all_genres),
                        isItemSelected = true
                    )
                genreItemList.add(initialGenreListItem)
                genreListMap.onEachIndexed { index, entry ->
                    val item = MovieFilterDialogItem(
                        itemCategory = FilterDialogItemCategory.GENRE,
                        id = index + 1,
                        itemCode = entry.key,
                        itemName = entry.value,
                        isItemSelected = false
                    )
                    genreItemList.add(item)
                }
                movieFilterItem.genresFilterItem.forEach { savedListItem ->
                    genreItemList.replaceAll {
                        if (it.id == savedListItem.id) {
                            it.copy(
                                isItemSelected = true
                            )
                        } else {
                            it.copy(
                                isItemSelected = false
                            )
                        }
                    }
                }
                _movieFilterGenreListMutableStateFlow.emit(genreItemList)
        }.launchIn(viewModelScope)
    }

    fun initFilterItems(movieFilterItem: MovieFilterItem) {
        viewModelScope.launch {
            _movieFilterRegionListMutableStateFlow.emit(movieFilterUtils.getMovieFilterRegionItems(movieFilterItem))
            _movieFilterPeriodListMutableStateFlow.emit(movieFilterUtils.getMovieFilterTimeItems(movieFilterItem))
            _movieFilterSortListMutableStateFlow.emit(movieFilterUtils.getMovieFilterSortItems(movieFilterItem))
        }
    }

    val myClickListener = object : MyClickListeners<MovieFilterDialogItem> {
        override fun click(item: MovieFilterDialogItem) {
            when (item.itemCategory) {
                FilterDialogItemCategory.GENRE -> filterDialogGenreItemSelect(item)
                FilterDialogItemCategory.REGIONS -> filterDialogRegionItemSelect(item)
                FilterDialogItemCategory.TIME -> filterDialogPeriodItemSelect(item)
                FilterDialogItemCategory.SORT -> filterDialogSortItemSelect(item)
            }
        }
    }

    private fun filterDialogGenreItemSelect(item: MovieFilterDialogItem) {
        viewModelScope.launch {
            val currentList = _movieFilterGenreListMutableStateFlow.value
            val list = _movieFilterGenreListMutableStateFlow.value.map { movieFilterDialogItem ->
                if (item.id == 0) {
                    if (!item.isItemSelected) {
                        if (currentList.filter { it.id > 0 }.any { it.isItemSelected }) {
                            if (movieFilterDialogItem.id > 0) {
                                movieFilterDialogItem.copy(
                                    isItemSelected = false
                                )
                            } else {
                                movieFilterDialogItem.copy(
                                    isItemSelected = true
                                )
                            }
                        } else {
                            if (movieFilterDialogItem.id > 0) {
                                movieFilterDialogItem.copy(
                                    isItemSelected = false
                                )
                            } else {
                                movieFilterDialogItem.copy(
                                    isItemSelected = true
                                )
                            }
                        }
                    } else {
                        movieFilterDialogItem.copy(
                            isItemSelected = false
                        )
                    }
                } else {
                    if (item.id == movieFilterDialogItem.id) {
                        if (item.isItemSelected) {
                            movieFilterDialogItem.copy(isItemSelected = false)
                        } else {
                            movieFilterDialogItem.copy(isItemSelected = true)
                        }
                    } else {
                        if (movieFilterDialogItem.id == 0 && movieFilterDialogItem.isItemSelected){
                            movieFilterDialogItem.copy(
                                isItemSelected = false
                            )
                        } else {
                            movieFilterDialogItem
                        }
                    }
                }
            }
            checkEnabledSave(list)
            _movieFilterGenreListMutableStateFlow.emit(list)
        }
    }

    private fun filterDialogRegionItemSelect(item: MovieFilterDialogItem) {
        viewModelScope.launch {
            val list = _movieFilterRegionListMutableStateFlow.value.map { movieFilterDialogItem ->
                if (movieFilterDialogItem.id == item.id) {
                    if (item.isItemSelected) {
                        movieFilterDialogItem.copy(isItemSelected = false)
                    } else {
                        movieFilterDialogItem.copy(isItemSelected = true)
                    }
                } else {
                    movieFilterDialogItem.copy(isItemSelected = false)
                }
            }
            checkEnabledSave(list)
            _movieFilterRegionListMutableStateFlow.emit(list)
        }
    }

    private fun filterDialogPeriodItemSelect(item: MovieFilterDialogItem) {
        viewModelScope.launch {
            val list = _movieFilterPeriodListMutableStateFlow.value.map { movieFilterDialogItem ->
                if (movieFilterDialogItem.id == item.id) {
                    if (item.isItemSelected) {
                        movieFilterDialogItem.copy(isItemSelected = false)
                    } else {
                        movieFilterDialogItem.copy(isItemSelected = true)
                    }
                } else {
                    movieFilterDialogItem.copy(isItemSelected = false)
                }
            }
            checkEnabledSave(list)
            _movieFilterPeriodListMutableStateFlow.emit(list)
        }
    }

    private fun filterDialogSortItemSelect(item: MovieFilterDialogItem) {
        viewModelScope.launch {
            val list = _movieFilterSortListMutableStateFlow.value.map { movieFilterDialogItem ->
                if (movieFilterDialogItem.id == item.id) {
                    if (item.isItemSelected) {
                        movieFilterDialogItem.copy(isItemSelected = false)
                    } else {
                        movieFilterDialogItem.copy(isItemSelected = true)
                    }
                } else {
                    movieFilterDialogItem.copy(isItemSelected = false)
                }
            }
            _movieFilterSortListMutableStateFlow.emit(list)
        }
    }

    private suspend fun checkEnabledSave(list: List<MovieFilterDialogItem>) {
        _isEnabledMovieFilterApplyButton.emit(
            list.any { it.isItemSelected }
        )
    }

    fun applyFilter() : MovieFilterItem {
        val regionFilterItem = _movieFilterRegionListMutableStateFlow.value.filter { it.isItemSelected }[0]
        val genresFilterItem = _movieFilterGenreListMutableStateFlow.value.filter { it.isItemSelected }
        val timeFilterItem = _movieFilterPeriodListMutableStateFlow.value.filter { it.isItemSelected }[0]
        val sortFilterItem = _movieFilterSortListMutableStateFlow.value.find { it.isItemSelected }
        return MovieFilterItem(
            regionFilterItem = regionFilterItem,
            genresFilterItem = genresFilterItem,
            timeFilterItem = timeFilterItem,
            sortFilterItem = sortFilterItem
        )
    }

    companion object {
        private const val SAVED_MOVIE_FILTER_ITEM = "savedMovieFilterItem"
        private val TAG = ExploreMovieFilterDialogViewModel::class.java.simpleName
    }
}