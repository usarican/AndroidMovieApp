package com.example.mymovieapp.features.auth.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.R
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.core.ui.event.DialogDismissEvent
import com.example.mymovieapp.core.ui.event.MyEvent
import com.example.mymovieapp.features.auth.domain.model.AuthUserDataStateModel
import com.example.mymovieapp.features.auth.domain.usecase.FirebaseCreateNewUserUseCase
import com.example.mymovieapp.features.auth.domain.usecase.FirebaseSignInWithEmailAndPasswordUseCase
import com.example.mymovieapp.features.auth.domain.usecase.FirebaseUpdateUserInformationUseCase
import com.example.mymovieapp.features.dialog.ErrorDialog
import com.example.mymovieapp.features.dialog.SuccessDialog
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val genreListUseCase: GetGenreListUseCase,
    private val downloadWorkManager: DownloadWorkManager,
    private val firebaseCreateNewUserUseCase: FirebaseCreateNewUserUseCase,
    private val firebaseSignInWithEmailAndPasswordUseCase: FirebaseSignInWithEmailAndPasswordUseCase,
    private val firebaseUpdateUserInformationUseCase: FirebaseUpdateUserInformationUseCase
) : BaseViewModel() {
    init {
        downloadWorkManager.startDownloadMovieGenres()
    }

    val viewPagerCurrentPage = MutableStateFlow(0)

    fun resetViewPagerCurrentPage() {
        viewModelScope.launch {
            viewPagerCurrentPage.emit(0)
        }
    }

    private val _navigateLoginPage = MutableLiveData<Boolean?>(null)
    fun navigateLoginPage(): LiveData<Boolean?> = _navigateLoginPage
    fun setNavigateLoginPageToFalse() {
        _navigateLoginPage.value = false
    }

    private val _userEnteredOnce = MutableLiveData<Boolean?>(null)
    fun userEnteredOnce(): LiveData<Boolean?> = _userEnteredOnce
    fun setUserEnteredOnceToNull() {
        _userEnteredOnce.value = null
    }

    private val authUserDataStateModel = AuthUserDataStateModel()

    private val _authUserDataMutableStateFlow = MutableStateFlow(authUserDataStateModel)
    fun getAuthUserDataStateFlow(): StateFlow<AuthUserDataStateModel> =
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

    private fun updateAuthUserData(authUserDataStateModel: AuthUserDataStateModel) {
        viewModelScope.launch {
            _authUserDataMutableStateFlow.emit(authUserDataStateModel)
        }
    }

    fun setUserEmail(userEmail: String?) {
        authUserDataStateModel.userMail = userEmail
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserProfilePicture(picture: Uri?) {
        authUserDataStateModel.userProfilePicture = picture
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserFullName(userFullName: String?) {
        authUserDataStateModel.userFullName = userFullName
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserNickName(userNickName: String?) {
        authUserDataStateModel.userNickName = userNickName
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserGenre(userGenre: String?) {
        authUserDataStateModel.userGenre = userGenre
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserPhoneNumber(userPhoneNumber: String?) {
        authUserDataStateModel.userPhoneNumber = userPhoneNumber
        updateAuthUserData(authUserDataStateModel)
    }

    fun setUserMovieGenreInterestList() {
        authUserDataStateModel.userMovieGenreInterestList =
            _movieGenreListMutableStateFlow.value.filter { it.isItemSelected }
        updateAuthUserData(authUserDataStateModel)
    }

    fun createNewUser(email: String, password: String) {
        firebaseCreateNewUserUseCase.createNewUser(email, password)
            .doOnSuccess {
                showLoading.value = false
                showDialog.value = SuccessDialog(
                    dialogTag = CREATE_ACCOUNT_SUCCESS_DIALOG_TAG,
                    title = R.string.create_account_success_dialog_title,
                    message = R.string.create_account_success_dialog_content,
                    buttonStrRes = R.string.continue_button
                )
            }
            .doOnLoading {
                showLoading.value = true
            }
            .doOnError {
                showLoading.value = false
                showDialog.value = ErrorDialog(
                    dialogTag = CREATE_ACCOUNT_ERROR_DIALOG_TAG,
                    titleStrRes = R.string.create_account_failure_dialog_title,
                    message = it.message ?: "Unexpected Error Occurs.",
                    buttonStrRes = R.string.got_it
                )
            }
            .launchIn(viewModelScope)
    }

    fun signInWithFirebaseEmailAndPassword(email: String, password: String) {
        firebaseSignInWithEmailAndPasswordUseCase.signInWithEmailAndPassword(email, password)
            .doOnSuccess {
                showLoading.value = false
                authUserDataStateModel.userUid = it?.userUid ?: UUID.randomUUID().toString()
                _userEnteredOnce.value = it?.userEnteredFirstTime
            }
            .doOnLoading {
                showLoading.value = true
            }
            .doOnError {
                showLoading.value = false
                showDialog.value = ErrorDialog(
                    dialogTag = SIGN_IN_WITH_EMAIL_AND_PASSWORD_ERROR_DIALOG_TAG,
                    titleStrRes = R.string.sign_in_failure_dialog_title,
                    message = it.message ?: "Unexpected Error Occurs.",
                    buttonStrRes = R.string.got_it
                )
            }
            .launchIn(viewModelScope)
    }

    fun updateUserInformation() {
        firebaseUpdateUserInformationUseCase.updateUser(authUserDataStateModel)
            .doOnSuccess {
                showLoading.value = false
            }
            .doOnLoading {
                showLoading.value = true
            }
            .doOnError {
                showLoading.value = false
                showDialog.value = ErrorDialog(
                    dialogTag = SIGN_IN_WITH_EMAIL_AND_PASSWORD_ERROR_DIALOG_TAG,
                    titleStrRes = R.string.sign_in_failure_dialog_title,
                    message = it.message ?: "Unexpected Error Occurs.",
                    buttonStrRes = R.string.got_it
                )
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

    override fun onEventReceiver(myEvent: MyEvent) {
        when (myEvent) {
            is DialogDismissEvent -> {
                if (myEvent.dialogTag == CREATE_ACCOUNT_SUCCESS_DIALOG_TAG) {
                    _navigateLoginPage.value = true
                }
            }
        }
    }

    companion object {
        private val TAG = AuthenticationViewModel::class.java.simpleName
        private const val CREATE_ACCOUNT_SUCCESS_DIALOG_TAG = "CREATE_ACCOUNT_SUCCESS_DIALOG_TAG"
        private const val CREATE_ACCOUNT_ERROR_DIALOG_TAG = "CREATE_ACCOUNT_ERROR_DIALOG_TAG"
        private const val SIGN_IN_WITH_EMAIL_AND_PASSWORD_ERROR_DIALOG_TAG =
            "SIGN_IN_WITH_EMAIL_AND_PASSWORD_ERROR_DIALOG_TAG"
    }
}