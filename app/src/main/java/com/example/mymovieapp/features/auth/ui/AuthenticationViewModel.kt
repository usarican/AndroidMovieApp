package com.example.mymovieapp.features.auth.ui

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.local.AuthUserData
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.domain.model.User
import com.example.mymovieapp.features.auth.domain.usecase.FirebaseCreateNewUserUseCase
import com.example.mymovieapp.features.explore.ui.dialog.FilterDialogItemCategory
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import com.example.mymovieapp.features.home.ui.UserInterfaceState
import com.example.mymovieapp.utils.MyClickListeners
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
class AuthenticationViewModel @Inject constructor(
    val genreListUseCase: GetGenreListUseCase,
    private val downloadWorkManager: DownloadWorkManager,
    private val firebaseCreateNewUserUseCase: FirebaseCreateNewUserUseCase,
    private val firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp
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

    private val _createNewUserStateMutableFlow = MutableStateFlow<State<UserDto>>(State.Loading)
    fun createNewUserState() : StateFlow<State<UserDto>> = _createNewUserStateMutableFlow.asStateFlow()

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
    fun setUserPhoneNumber(userPhoneNumber : String?) {
        authUserData.userPhoneNumber = userPhoneNumber
        updateAuthUserData(authUserData)
    }
    fun setUserMovieGenreInterestList() {
        authUserData.userMovieGenreInterestList = _movieGenreListMutableStateFlow.value.filter { it.isItemSelected }
        updateAuthUserData(authUserData)
    }

    fun createNewUser(email : String, password : String) {
        firebaseCreateNewUserUseCase.createNewUser(email,password)
            .doOnSuccess {
                firebaseFirestoreRepositoryImp.insertNewUser(it)
                    .onEach { Timber.tag("UTKU").d("Firestore state = $it")}
                    .launchIn(viewModelScope)
            }
            .onEach { state ->
                if (state is State.Success) {
                    Timber.tag("UTKU").d(state.data.toString())
                }
                _createNewUserStateMutableFlow.emit(state)
            }
            .launchIn(viewModelScope)
    }



    fun getGenreList(language: String,movieFilterItem: List<MovieFilterDialogItem>?) {
        genreListUseCase.invoke(language)
            .doOnLoading {
                _layoutViewState.emit(LayoutViewState(UserInterfaceState.DisplayLoading))
            }
            .doOnSuccess { list ->
                var genreItemList = mutableListOf<MovieFilterDialogItem>()
                list.onEachIndexed { index, genreResponse ->
                    val item = MovieFilterDialogItem(
                        itemCategory = FilterDialogItemCategory.GENRE,
                        id = index + 1,
                        itemCode = genreResponse.id,
                        itemName = genreResponse.name,
                        isItemSelected = false
                    )
                    genreItemList.add(item)

                    movieFilterItem?.forEach { savedListItem ->
                        genreItemList = genreItemList.map {
                            if (it.id == savedListItem.id) {
                                it.copy(
                                    isItemSelected = true
                                )
                            } else {
                                it
                            }
                        } as MutableList<MovieFilterDialogItem>
                    }
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