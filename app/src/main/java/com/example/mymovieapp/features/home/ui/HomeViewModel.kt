package com.example.mymovieapp.features.home.ui

import androidx.lifecycle.*
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.home.domain.model.CategoryList
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.features.home.domain.usecase.GetCategoryMoviesUseCase
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getCategoryMoviesUseCase: GetCategoryMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _trendingMoviesOfWeekMutableLiveData = MutableLiveData<List<Movie>>()
    fun getTrendingMoviesOfWeekLiveData() : LiveData<List<Movie>> = _trendingMoviesOfWeekMutableLiveData

    private val _categoryMoviesMutableStateFlow = MutableStateFlow<CategoryList?>(null)
    fun getCategoryMoviesStateFlow() : StateFlow<CategoryList?> = _categoryMoviesMutableStateFlow.asStateFlow()

    private val _trendingMoviesOfWeekStateMutableLiveData = MutableLiveData<LayoutViewState>()
    fun getTrendingMoviesOfWeekStateLiveData() : LiveData<LayoutViewState> = _trendingMoviesOfWeekStateMutableLiveData


    val viewPagerSavedState : StateFlow<Int> = savedStateHandle.getStateFlow(
        VIEW_PAGER_2_STATE,
        0
    )

    fun setViewPagerSavedState(index : Int){
        savedStateHandle[VIEW_PAGER_2_STATE] = index
    }

    fun getTrendMoviesOfWeek() {
        getTrendingMoviesUseCase.invoke("en")
            .doOnSuccess { movieList ->
                _trendingMoviesOfWeekMutableLiveData.value = movieList
            }.doOnError {
                Timber.tag(TAG).e(it)
            }
            .onEach { state ->
                _trendingMoviesOfWeekStateMutableLiveData.value = LayoutViewState(state)
            }
            .launchIn(viewModelScope)
    }

    fun getCategoryMoviesList() {
        getCategoryMoviesUseCase.invoke("en",viewModelScope)
            .doOnSuccess { categoryList ->
                viewModelScope.launch(Dispatchers.IO) {
                    _categoryMoviesMutableStateFlow.emit(categoryList)
                }
            }
            .launchIn(viewModelScope)
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
        const val VIEW_PAGER_2_STATE = "ViewPager2State"
    }
}