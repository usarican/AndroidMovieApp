package com.example.mymovieapp.features.home.ui

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.core.ui.event.MyEvent
import com.example.mymovieapp.core.ui.event.RetryNetworkCallEvent
import com.example.mymovieapp.features.details.domain.usecase.MovieDetailUseCase
import com.example.mymovieapp.features.dialog.BannerMovieDetailDialog
import com.example.mymovieapp.features.dialog.BannerMovieItemDetailDialogFragment
import com.example.mymovieapp.features.dialog.DialogManager
import com.example.mymovieapp.features.home.domain.model.*
import com.example.mymovieapp.features.home.domain.usecase.GetCategoryMoviesUseCase
import com.example.mymovieapp.features.home.domain.usecase.GetTrendingMoviesUseCase
import com.example.mymovieapp.utils.BannerMovieItemClickListener
import com.example.mymovieapp.utils.FragmentUtils
import com.example.mymovieapp.utils.NetworkUtils
import com.example.mymovieapp.utils.PagingLoadStateCallBack
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getCategoryMoviesUseCase: GetCategoryMoviesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val networkUtils: NetworkUtils,
    private val fragmentUtils: FragmentUtils,
    private val movieDetailUseCase: MovieDetailUseCase
) : BaseViewModel() {

    private val handler = CoroutineExceptionHandler { context, exception ->
        viewModelScope.launch {
            delay(UI_STATE_DELAY)
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayError(exception)
                )
            )
        }
    }

    private var networkSearchingJob : Job? = null

    private var trendingMovieState : UserInterfaceState = UserInterfaceState.DisplayLoading

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
                trendingMovieState = UserInterfaceState.DisplayUI
            }.doOnError {
                viewModelScope.launch {
                    delay(UI_STATE_DELAY)
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

    fun setUIStateLoading(){
        viewModelScope.launch {
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
        }
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
                    delay(UI_STATE_DELAY)
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

            if (categoryMoviesUIState.categoryMoviesIsDisplay() && trendingMovieState == UserInterfaceState.DisplayUI) {
                viewModelScope.launch {
                    delay(UI_STATE_DELAY * 2)
                    _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayUI))
                }
            }
        }

        override fun doOnError(error: Throwable, categoryType: CategoryType) {
            Timber.tag(TAG).d("Paging Adapter Is Error ${categoryType.name}")
            viewModelScope.launch {
                delay(UI_STATE_DELAY)
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

    private fun retryNetworkCall() {
        viewModelScope.launch {
            _homeStateMutableStateFlow.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
            trendingMovieState = UserInterfaceState.DisplayLoading
            var retryTimeOut = 0L
            var waitingFotNetworkConnection = true
            networkSearchingJob = CoroutineScope(Dispatchers.IO).launch(handler) {
                while (waitingFotNetworkConnection) {
                    if (networkUtils.isNetworkAvailable()) {
                        withContext(Dispatchers.Main) { _refreshFragmentMutableLiveData.value = true }
                        waitingFotNetworkConnection = false
                    } else {
                        if (retryTimeOut > 3000){
                            throw IOException()
                        }
                    }
                    retryTimeOut += 500L
                    delay(SEARCHING_NETWORK_DELAY)
                }
                throw Exception()
            }
        }
    }

    val bannerMovieItemClickListener = object : BannerMovieItemClickListener {
        override fun clickBannerMovie(movieId: Int) {
            showLoading.value = true
            movieDetailUseCase.getMovieDetail(movieId)
                .doOnSuccess { movieDetailItem ->
                    showLoading.value = false
                    showDialog.value = BannerMovieDetailDialog(movieDetailItem)
                }
                .doOnError {
                    showLoading.value = false
                    Timber.tag(TAG).d("Banner Movie Error = ${it.message}")
                }
                .launchIn(viewModelScope)
        }

    }

    fun refreshFragment(fragmentManager: FragmentManager?){
        fragmentManager?.let {
            fragmentUtils.refreshFragment(it)
        } ?: viewModelScope.launch {
            delay(UI_STATE_DELAY)
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
        private const val UI_STATE_DELAY = 500L
        private const val SEARCHING_NETWORK_DELAY = 500L
    }
}