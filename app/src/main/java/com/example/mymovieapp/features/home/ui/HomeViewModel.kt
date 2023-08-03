package com.example.mymovieapp.features.home.ui

import androidx.lifecycle.*
import com.example.mymovieapp.features.home.domain.model.*
import com.example.mymovieapp.features.home.domain.usecase.GetCategoryMoviesUseCase
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.PagingLoadStateCallBack
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val homeUIState = HomeUIState(
        trendingMoviesState = UserInterfaceState.DisplayLoading,
        categoryMoviesState = UserInterfaceState.DisplayLoading
    )

    private val categoryMoviesUIState = CategoryMoviesUIState(
        topRatedMoviesState = UserInterfaceState.DisplayLoading,
        upComingMoviesState = UserInterfaceState.DisplayLoading,
        popularMoviesState = UserInterfaceState.DisplayLoading
    )




    private val _trendingMoviesOfWeekMutableLiveData = MutableLiveData<List<Movie>>()
    fun getTrendingMoviesOfWeekLiveData() : LiveData<List<Movie>> = _trendingMoviesOfWeekMutableLiveData

    private val _categoryMoviesMutableStateFlow = MutableStateFlow<CategoryList?>(null)
    fun getCategoryMoviesStateFlow() : StateFlow<CategoryList?> = _categoryMoviesMutableStateFlow.asStateFlow()

    private val _homeStateMutableStateFlow = MutableStateFlow<UserInterfaceState>(UserInterfaceState.DisplayLoading)
    fun getHomeState() : StateFlow<UserInterfaceState> = _homeStateMutableStateFlow.asStateFlow()


    val viewPagerSavedState : StateFlow<Int> = savedStateHandle.getStateFlow(
        VIEW_PAGER_2_STATE,
        0
    )

    fun setViewPagerSavedState(index : Int){
        savedStateHandle[VIEW_PAGER_2_STATE] = index
    }

    fun getTrendMoviesOfWeek() {
        getTrendingMoviesUseCase.invoke("en")
            .doOnLoading {
                viewModelScope.launch {
                    _homeStateMutableStateFlow.emit(UserInterfaceState.DisplayLoading)
                }
            }
            .doOnSuccess { movieList ->
                _trendingMoviesOfWeekMutableLiveData.value = movieList
                homeUIState.trendingMoviesState = UserInterfaceState.DisplayUI
            }.doOnError{
                homeUIState.trendingMoviesState = UserInterfaceState.DisplayError(error = it)
            }
            .launchIn(viewModelScope)
    }

    fun getCategoryMoviesList() {
        getCategoryMoviesUseCase.invoke("en",viewModelScope)
            .doOnSuccess { categoryList ->
                viewModelScope.launch {
                    _categoryMoviesMutableStateFlow.emit(categoryList)
                }
            }.launchIn(viewModelScope)
    }

    val pagingLoadStateCallBack = object : PagingLoadStateCallBack {
        override fun doOnLoading(categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Loading...${categoryType.name}")
            viewModelScope.launch {
                _homeStateMutableStateFlow.emit(UserInterfaceState.DisplayLoading)
            }
        }

        override fun doOnSuccess(categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Success ${categoryType.name}")
            when(categoryType){
                CategoryType.POPULAR -> categoryMoviesUIState.popularMoviesState = UserInterfaceState.DisplayUI
                CategoryType.UP_COMING -> categoryMoviesUIState.upComingMoviesState = UserInterfaceState.DisplayUI
                CategoryType.TOP_RATED -> categoryMoviesUIState.topRatedMoviesState = UserInterfaceState.DisplayUI
            }
            Timber.tag(TAG).d("$categoryMoviesUIState ")

            if (categoryMoviesUIState.categoryMoviesIsDisplay() && homeUIState.trendingMoviesState is UserInterfaceState.DisplayUI) {
                viewModelScope.launch {
                    _homeStateMutableStateFlow.emit(UserInterfaceState.DisplayUI)
                }
            }
        }

        override fun doOnError(error : Throwable, categoryType: CategoryType) {
            viewModelScope.launch {
                _homeStateMutableStateFlow.emit(UserInterfaceState.DisplayError(error))
            }
        }

    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
        const val VIEW_PAGER_2_STATE = "ViewPager2State"
    }
}