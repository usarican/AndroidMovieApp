package com.example.mymovieapp.features.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel(){

    private val _trendingMoviesOfWeekMutableLiveData = MutableLiveData<List<Movie>>()
    fun getTrendingMoviesOfWeekLiveData() : LiveData<List<Movie>> = _trendingMoviesOfWeekMutableLiveData

    private val _trendingMoviesOfWeekStateMutableLiveData = MutableLiveData<LayoutViewState>()
    fun getTrendingMoviesOfWeekStateLiveData() : LiveData<LayoutViewState> = _trendingMoviesOfWeekStateMutableLiveData


    fun getTrendMoviesOfWeek() {
        getTrendingMoviesUseCase.invoke("en")
            .doOnSuccess { movieList ->
                _trendingMoviesOfWeekMutableLiveData.value = movieList
            }
            .onEach { state ->
                _trendingMoviesOfWeekStateMutableLiveData.value = LayoutViewState(state)
            }
            .launchIn(viewModelScope)
    }
}