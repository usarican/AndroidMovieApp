package com.example.mymovieapp.features.explore.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.explore.domain.usecase.DiscoveryMoviesUseCase
import com.example.mymovieapp.features.explore.domain.usecase.ExploreMoviesUseCase
import com.example.mymovieapp.features.explore.domain.usecase.SeeAllMoviesUseCase
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterItem
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterUtils
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.utils.extensions.doOnSuccess
import com.example.mymovieapp.utils.extensions.separateIntValueWithCommaAndReturnString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exploreMoviesUseCase: ExploreMoviesUseCase,
    private val seeAllMoviesUseCase: SeeAllMoviesUseCase,
    private val discoveryMoviesUseCase: DiscoveryMoviesUseCase,
    private val movieFilterUtils: MovieFilterUtils,
    private val genreListUseCase: GetGenreListUseCase
) : BaseViewModel() {

    private val searchStringInput = MutableStateFlow<String?>(null)

    val savedMovieFilterItem = savedStateHandle.getStateFlow(
        SAVED_MOVIE_FILTER_ITEM_EXPLORE_VIEW_MODEL,movieFilterUtils.getInitialMovieFilterItem())

    val movieGenreList = MutableStateFlow<List<GenreResponse>>(emptyList())

    fun setMovieFilterItem(movieFilterItem: MovieFilterItem?) {
        savedStateHandle[SAVED_MOVIE_FILTER_ITEM_EXPLORE_VIEW_MODEL] = movieFilterItem
    }

    fun setSearchStringInput(searchString : String?){
        viewModelScope.launch {
            searchStringInput.emit(searchString)
        }
    }

    fun getGenreList(language: String) = genreListUseCase.invoke(language).doOnSuccess {
        movieGenreList.emit(it)
    }.launchIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getDiscoveryMovieList(language: String) : Flow<PagingData<Movie>?> {
        return savedMovieFilterItem.flatMapLatest { movieFilterItem ->
            discoveryMoviesUseCase.invoke(
                language = language,
                movieFilterItem = movieFilterItem
            ).cachedIn(viewModelScope)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSearchingMovieList(language: String) : Flow<PagingData<Movie>?> {
        return searchStringInput.flatMapLatest {
            exploreMoviesUseCase.getSearchingMovieResults(it,language)
        }
    }

    fun getMoviesList(categoryType: CategoryType,language : String) : Flow<PagingData<Movie>> {
        return seeAllMoviesUseCase.invoke(categoryType,language).cachedIn(viewModelScope)
    }



    companion object {
        private val TAG = ExploreViewModel::class.java.simpleName
        private const val SEARCHING_DELAY_TIME = 500L
        private const val SAVED_MOVIE_FILTER_ITEM_EXPLORE_VIEW_MODEL = "SAVED_MOVIE_FILTER_ITEM_EXPLORE_VIEW_MODEL"
    }
}