package com.example.mymovieapp.features.details.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.features.details.domain.model.MovieDetailPageItem
import com.example.mymovieapp.features.details.domain.usecase.DetailPageFetchAllDataUseCase
import com.example.mymovieapp.features.details.domain.usecase.MovieDetailUseCase
import com.example.mymovieapp.features.home.ui.HomeViewModel
import com.example.mymovieapp.features.home.ui.UserInterfaceState
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailPageFetchAllDataUseCase: DetailPageFetchAllDataUseCase
): BaseViewModel() {

    private val _movieDetailLayoutViewState = MutableStateFlow(LayoutViewState(UserInterfaceState.DisplayLoading))
    fun getMovieDetailLayoutViewStateFlow() : StateFlow<LayoutViewState> = _movieDetailLayoutViewState.asStateFlow()

    private val _movieDetailInformation = MutableLiveData<MovieDetailPageItem>()
    fun getMovieDetailInformationLiveData() : LiveData<MovieDetailPageItem> = _movieDetailInformation


    fun getMovieDetailInformation(movieId : Int,language : String) {
        movieDetailPageFetchAllDataUseCase.invoke(movieId,viewModelScope,language)
            .doOnLoading {
                viewModelScope.launch {
                    _movieDetailLayoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
                }
            }
            .doOnSuccess { movieDetailPageItem ->
                if (movieDetailPageItem != null) {
                    _movieDetailInformation.value = movieDetailPageItem
                    viewModelScope.launch {
                        delay(UI_STATE_DELAY * 2)
                        _movieDetailLayoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayUI))
                    }
                } else {
                    viewModelScope.launch {
                        _movieDetailLayoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayError(
                            Throwable("Somethings Gone Wrong...")
                        )))
                    }
                }
            }
            .doOnError {
                viewModelScope.launch {
                    _movieDetailLayoutViewState.emit(
                        LayoutViewState(
                            UserInterfaceState.DisplayError(
                                it
                            )
                        )
                    )
                }
            }
            .onEach {
                Timber.tag("MovieDetailViewModel").d("States : $it ")
            }
            .launchIn(viewModelScope)
    }
    companion object {
        private const val UI_STATE_DELAY = 500L
    }
}