package com.example.mymovieapp.features.home.ui

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.core.ui.event.MyEvent
import com.example.mymovieapp.core.ui.event.RetryNetworkCallEvent
import com.example.mymovieapp.features.home.domain.model.*
import com.example.mymovieapp.features.home.domain.usecase.GetCategoryMoviesUseCase
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.FragmentUtils
import com.example.mymovieapp.utils.NetworkUtils
import com.example.mymovieapp.utils.PagingLoadStateCallBack
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getCategoryMoviesUseCase: GetCategoryMoviesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val networkUtils: NetworkUtils,
    private val fragmentUtils: FragmentUtils
) : BaseViewModel() {

    private val handler = CoroutineExceptionHandler { context, exception ->
        viewModelScope.launch {
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(exception)
                )
            )
        }
    }

    private var networkSearchingJob : Job? = null

    private val homeUIState = HomeUIState(
        trendingMoviesState = UserInterfaceState.DisplayLoading,
        categoryMoviesState = UserInterfaceState.DisplayLoading
    )

    private val categoryMoviesUIState = CategoryMoviesUIState(
        topRatedMoviesState = UserInterfaceState.DisplayLoading,
        upComingMoviesState = UserInterfaceState.DisplayLoading,
        popularMoviesState = UserInterfaceState.DisplayLoading
    )

    private val _refreshFragmentMutableLiveData = MutableLiveData(false)
    fun getRefreshFragmentLiveData() : LiveData<Boolean> = _refreshFragmentMutableLiveData
    fun setRefreshFragmentLiveData(isRefresh : Boolean) { _refreshFragmentMutableLiveData.value = isRefresh}
    private val _trendingMoviesOfWeekMutableLiveData = MutableLiveData<List<Movie>>()
    fun getTrendingMoviesOfWeekLiveData(): LiveData<List<Movie>> =
        _trendingMoviesOfWeekMutableLiveData

    private val _categoryMoviesMutableStateFlow = MutableStateFlow<CategoryList?>(null)
    fun getCategoryMoviesStateFlow(): StateFlow<CategoryList?> =
        _categoryMoviesMutableStateFlow.asStateFlow()

    private val _homeStateMutableStateFlow =
        MutableStateFlow(LayoutViewState(UserInterfaceState.DisplayLoading))

    fun getHomeState(): StateFlow<LayoutViewState> = _homeStateMutableStateFlow.asStateFlow()


    val viewPagerSavedState: StateFlow<Int> = savedStateHandle.getStateFlow(
        VIEW_PAGER_2_STATE,
        0
    )

    fun setViewPagerSavedState(index: Int) {
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
            }.doOnError {
                viewModelScope.launch {
                    delay(1000L)
                    _homeStateMutableStateFlow.emit(
                        LayoutViewState(
                            UserInterfaceState.DisplayError(
                                it
                            )
                        )
                    )
                }
            }.onEach {
                Timber.tag("TAG").d("TrendingMovieList State : $it")
            }
            .launchIn(viewModelScope)
    }

    fun getCategoryMoviesList() {
        getCategoryMoviesUseCase.invoke("en", viewModelScope)
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
                    _homeStateMutableStateFlow.emit(
                        LayoutViewState(
                            UserInterfaceState.DisplayError(
                                it
                            )
                        )
                    )
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
            when (categoryType) {
                CategoryType.POPULAR -> categoryMoviesUIState.popularMoviesState =
                    UserInterfaceState.DisplayUI
                CategoryType.UP_COMING -> categoryMoviesUIState.upComingMoviesState =
                    UserInterfaceState.DisplayUI
                CategoryType.TOP_RATED -> categoryMoviesUIState.topRatedMoviesState =
                    UserInterfaceState.DisplayUI
            }

            if (categoryMoviesUIState.categoryMoviesIsDisplay() && homeUIState.trendingMoviesState == UserInterfaceState.DisplayUI) {
                viewModelScope.launch {
                    delay(1000L)
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayUI))
                }
            }
        }

        override fun doOnError(error: Throwable, categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Error ${categoryType.name}")
            viewModelScope.launch {
                delay(1000L)
                _homeStateMutableStateFlow.emit(
                    LayoutViewState(
                        UserInterfaceState.DisplayError(
                            error
                        )
                    )
                )
            }
        }
    }

    private fun setUIStatesLoading() {
        homeUIState.trendingMoviesState = UserInterfaceState.DisplayLoading
        homeUIState.categoryMoviesState = UserInterfaceState.DisplayLoading

        categoryMoviesUIState.topRatedMoviesState = UserInterfaceState.DisplayLoading
        categoryMoviesUIState.upComingMoviesState = UserInterfaceState.DisplayLoading
        categoryMoviesUIState.popularMoviesState = UserInterfaceState.DisplayLoading

    }

    private fun retryNetworkCall() {
        viewModelScope.launch {
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
            setUIStatesLoading()
            var retryTimeOut = 0L
            var waitingFotNetworkConnection = true
            networkSearchingJob = CoroutineScope(Dispatchers.IO).launch(handler) {
                while (waitingFotNetworkConnection) {
                    if (networkUtils.isNetworkAvailable()) {
                        withContext(Dispatchers.Main) { _refreshFragmentMutableLiveData.value = true }
                        waitingFotNetworkConnection = false
                    } else {
                        if (retryTimeOut > 3000){
                            throw Throwable()
                        }
                    }
                    retryTimeOut += 500L
                    delay(500L)
                }
                throw Exception()
            }
        }
    }

    fun refreshFragment(fragmentManager: FragmentManager?){
        fragmentManager?.let {
            fragmentUtils.refreshFragment(it)
        } ?: viewModelScope.launch {
            delay(1000L)
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(Throwable())))
        }
    }

    fun loadingTimeOut(layoutViewState: LayoutViewState){
        if (layoutViewState.isLoading()){
            viewModelScope.launch {
                _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(Throwable())))
            }
        }
    }

    override fun onEventReceiver(myEvent: MyEvent) {
        when (myEvent) {
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