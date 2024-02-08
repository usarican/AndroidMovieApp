package com.example.mymovieapp.features.auth.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.auth.data.AuthUserData
import com.example.mymovieapp.features.explore.ui.dialog.FilterDialogItemCategory
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.features.home.ui.UserInterfaceState
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val genreListUseCase: GetGenreListUseCase,
    private val downloadWorkManager: DownloadWorkManager
) : BaseViewModel() {
    init {
        downloadWorkManager.startDownloadMovieGenres()
    }

    val viewPagerCurrentPage = MutableStateFlow(0)

    fun resetViewPagerCurrentPage(){
        viewModelScope.launch {
            viewPagerCurrentPage.emit(0)
        }
    }

    private val authUserData = AuthUserData()

    private val _authUserDataMutableStateFlow = MutableStateFlow(authUserData)
    fun getAuthUserDataStateFlow(): StateFlow<AuthUserData> =
        _authUserDataMutableStateFlow.asStateFlow()

    private val _layoutViewState =
        MutableStateFlow(LayoutViewState(UserInterfaceState.DisplayLoading))

    fun getLayoutViewState(): StateFlow<LayoutViewState> = _layoutViewState.asStateFlow()


    private val _movieGenreListMutableStateFlow =
        MutableStateFlow<List<MovieFilterDialogItem>>(emptyList())

    fun getMovieGenreListStateFlow(): StateFlow<List<MovieFilterDialogItem>> =
        _movieGenreListMutableStateFlow.asStateFlow()

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
    private fun updateAuthUserData(authUserData: AuthUserData) {
        viewModelScope.launch {
            _authUserDataMutableStateFlow.emit(authUserData)
        }
    }

    fun setUserEmail(userEmail : String?) {
        authUserData.userMail = userEmail
        updateAuthUserData(authUserData)
    }
    fun setUserProfilePicture(picture : Bitmap?) {
        authUserData.userProfilePicture = picture
        updateAuthUserData(authUserData)
    }
    fun setUserFullName(userFullName : String?) {
        authUserData.userFullName = userFullName
        updateAuthUserData(authUserData)
    }
    fun setUserNickName(userNickName : String?) {
        authUserData.userNickName = userNickName
        updateAuthUserData(authUserData)
    }
    fun setUserGenre(userGenre : String?) {
        authUserData.userGenre = userGenre
        updateAuthUserData(authUserData)
    }
    fun setUserMovieGenreInterestList(userMovieGenreInterestList : List<MovieFilterDialogItem>?) {
        authUserData.userMovieGenreInterestList = userMovieGenreInterestList
        updateAuthUserData(authUserData)
    }



    fun getGenreList(language: String) {
        genreListUseCase.invoke(language)
            .doOnLoading {
                _layoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
            }
            .doOnSuccess {
                val genreItemList = mutableListOf<MovieFilterDialogItem>()
                it.onEachIndexed { index, genreResponse ->
                    val item = MovieFilterDialogItem(
                        itemCategory = FilterDialogItemCategory.GENRE,
                        id = index + 1,
                        itemCode = genreResponse.id,
                        itemName = genreResponse.name,
                        isItemSelected = false
                    )
                    genreItemList.add(item)
                }
                _movieGenreListMutableStateFlow.emit(genreItemList)
                delay(1000L)
                _layoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayUI))
            }
            .doOnError {
                _layoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayError(it)))
            }
            .launchIn(viewModelScope)
    }

    companion object {
        private val TAG = AuthenticationViewModel::class.java.simpleName
    }
}