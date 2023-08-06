package com.example.mymovieapp.features.home.ui

import androidx.lifecycle.*
import com.example.mymovieapp.core.ui.EventListenerViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.core.ui.event.MyEvent
import com.example.mymovieapp.core.ui.event.RetryNetworkCallEvent
import com.example.mymovieapp.features.home.domain.model.*
import com.example.mymovieapp.features.home.domain.usecase.GetCategoryMoviesUseCase
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.PagingLoadStateCallBack
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getCategoryMoviesUseCase: GetCategoryMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
) : EventListenerViewModel(){

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

    private val _homeStateMutableStateFlow = MutableStateFlow(LayoutViewState(UserInterfaceState.DisplayLoading))
    fun getHomeState() : StateFlow<LayoutViewState> = _homeStateMutableStateFlow.asStateFlow()


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
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
                }
            }
            .doOnSuccess { movieList ->
                _trendingMoviesOfWeekMutableLiveData.value = movieList
                homeUIState.trendingMoviesState = UserInterfaceState.DisplayUI
            }.doOnError{
                viewModelScope.launch {
                    delay(1000L)
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(it)))
                }
            }.onEach {
                Timber.tag("TAG").d("TrendingMovieList State : $it")
            }
            .launchIn(viewModelScope)
    }

    fun getCategoryMoviesList() {
        getCategoryMoviesUseCase.invoke("en",viewModelScope)
            .doOnLoading {
                viewModelScope.launch {
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
                }
            }
            .doOnSuccess { categoryList ->
                viewModelScope.launch {
                    _categoryMoviesMutableStateFlow.emit(categoryList)
                }
            }
            .doOnError {
                viewModelScope.launch {
                    delay(1000L)
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(it)))
                }
            }
            .launchIn(viewModelScope)
    }

    val pagingLoadStateCallBack = object : PagingLoadStateCallBack {
        override fun doOnLoading(categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Loading...${categoryType.name}")
            viewModelScope.launch {
                _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
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

            if (categoryMoviesUIState.categoryMoviesIsDisplay() && homeUIState.trendingMoviesState == UserInterfaceState.DisplayUI) {
                viewModelScope.launch {
                    delay(1000L)
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayUI))
                }
            }
        }

        override fun doOnError(error : Throwable, categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Error ${categoryType.name}")
            viewModelScope.launch {
                delay(1000L)
                _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(error)))
            }
        }
    }

    fun setUIStatesLoading(){
        homeUIState.trendingMoviesState = UserInterfaceState.DisplayLoading
        homeUIState.categoryMoviesState = UserInterfaceState.DisplayLoading

        categoryMoviesUIState.topRatedMoviesState = UserInterfaceState.DisplayLoading
        categoryMoviesUIState.upComingMoviesState = UserInterfaceState.DisplayLoading
        categoryMoviesUIState.popularMoviesState = UserInterfaceState.DisplayLoading

    }

    private fun retryNetworkCall(){
        viewModelScope.launch {
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
            setUIStatesLoading()
            getTrendMoviesOfWeek()
            getCategoryMoviesList()
        }
    }

    override fun onEventReceiver(myEvent: MyEvent) {
        when(myEvent){
            is RetryNetworkCallEvent -> {
                retryNetworkCall()
            }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
        const val VIEW_PAGER_2_STATE = "ViewPager2State"
    }
}