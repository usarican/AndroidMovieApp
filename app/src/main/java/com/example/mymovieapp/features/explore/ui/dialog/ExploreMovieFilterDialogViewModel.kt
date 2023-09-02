package com.example.mymovieapp.features.explore.ui.dialog

import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.StringProvider
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreMovieFilterDialogViewModel @Inject constructor(
    private val genreListUseCase: GetGenreListUseCase,
    private val stringProvider: StringProvider,
    private val movieFilterUtils: MovieFilterUtils
) : BaseViewModel() {

    private val _movieFilterGenreListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())

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


    fun getGenreList(language: String) {
        val genreItemList = mutableListOf<MovieFilterDialogItem>()
        genreListUseCase.invoke(language).doOnSuccess { genreListMap ->
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
            _movieFilterGenreListMutableStateFlow.emit(genreItemList)
        }.onEach { state ->
            Timber.tag("aaaa").d(state.toString())
        }.launchIn(viewModelScope)
    }

    fun initFilterItems() {
        viewModelScope.launch {
            _movieFilterRegionListMutableStateFlow.emit(movieFilterUtils.getMovieFilterRegionItems())
            _movieFilterPeriodListMutableStateFlow.emit(movieFilterUtils.getMovieFilterTimeItems())
            _movieFilterSortListMutableStateFlow.emit(movieFilterUtils.getMovieFilterSortItems())
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

        /* Durumlar All Genre'ye basıldığında ve Diğerlerine Basıldığında
        All Genre'ye basıldıysa ve seçili değilse diğerlerinin seçimini kaldırıp all genreyi seç
        Eğer seçiliyse başka bir şey yapma
        Diğerlerine basıldığında all genre seçiliyse kapat
        eğer seçili değilse bir şey yapma/
         */
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
}