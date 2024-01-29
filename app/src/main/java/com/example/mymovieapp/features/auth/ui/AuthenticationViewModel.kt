package com.example.mymovieapp.features.auth.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.explore.ui.dialog.FilterDialogItemCategory
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val genreListUseCase: GetGenreListUseCase,
    private val database: MovieAppDatabase,
    private val downloadWorkManager: DownloadWorkManager
) : BaseViewModel() {
    init {
        downloadWorkManager.startDownloadMovieGenres()
    }

    fun clearDatabaseTable(){
        viewModelScope.launch(Dispatchers.IO){
            database.clearAllTables()
        }
    }


    private val _movieGenreListMutableStateFlow = MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())
    fun getMovieGenreListStateFlow() : StateFlow<List<MovieFilterDialogItem>> = _movieGenreListMutableStateFlow.asStateFlow()

    val genreListClickListener = object : MyClickListeners<MovieFilterDialogItem> {
        override fun click(item: MovieFilterDialogItem) {
            viewModelScope.launch {
                val list = _movieGenreListMutableStateFlow.value.map { movieFilterDialogItem ->
                    if (movieFilterDialogItem.id == item.id) {
                            movieFilterDialogItem.copy(isItemSelected = !movieFilterDialogItem.isItemSelected)
                    } else {
                        movieFilterDialogItem
                    }
                }
                _movieGenreListMutableStateFlow.emit(list)
            }
        }

    }
    fun getGenreList(language: String,savedGenreList : List<MovieFilterDialogItem>) {
            genreListUseCase.invoke(language).doOnSuccess {

                var genreItemList = mutableListOf<MovieFilterDialogItem>()

                it.onEachIndexed { index, entry ->
                    val item = MovieFilterDialogItem(
                        itemCategory = FilterDialogItemCategory.GENRE,
                        id = index + 1,
                        itemCode = entry.key,
                        itemName = entry.value,
                        isItemSelected = false
                    )
                    genreItemList.add(item)
                }
                savedGenreList.forEach { savedListItem ->
                    genreItemList = genreItemList.map {
                        if (it.id == savedListItem.id) {
                            it.copy(
                                isItemSelected = true
                            )
                        } else {
                            it
                        }
                    } as MutableList<MovieFilterDialogItem>
                }
                _movieGenreListMutableStateFlow.emit(genreItemList)
            }.onEach {
                Timber.tag(TAG).d("Genre List State $it")
            }.launchIn(viewModelScope)
    }

    companion object {
        private val TAG = AuthenticationViewModel::class.java.simpleName
    }
}