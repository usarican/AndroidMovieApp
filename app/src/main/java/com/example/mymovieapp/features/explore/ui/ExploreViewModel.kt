package com.example.mymovieapp.features.explore.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.explore.domain.usecase.ExploreMoviesUseCase
import com.example.mymovieapp.features.explore.domain.usecase.SeeAllMoviesUseCase
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.features.home.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exploreMoviesUseCase: ExploreMoviesUseCase,
    private val seeAllMoviesUseCase: SeeAllMoviesUseCase
) : BaseViewModel() {

    private val searchStringInput = MutableStateFlow<String?>(null)

    fun setSearchStringInput(searchString : String?){
        viewModelScope.launch {
            searchStringInput.emit(searchString)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSearchingMovieList(language: String) : Flow<PagingData<Movie>?> {
        return searchStringInput.flatMapLatest {
            exploreMoviesUseCase.getSearchingMovieResults(it,language)
        }
    }

    fun getMoviesList(categoryType: CategoryType,language : String) : Flow<PagingData<Movie>> {
        return seeAllMoviesUseCase.invoke(categoryType,language).map { pagingData ->
            //pagingData.filter { movie -> movie.releaseYear == "2022" }
            pagingData
        }.cachedIn(viewModelScope)
    }



    companion object {
        private val TAG = ExploreViewModel::class.java.simpleName
        private const val SEARCHING_DELAY_TIME = 500L
    }
}