package com.example.mymovieapp.features.splash.ui

import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseViewModel
import com.example.mymovieapp.features.dialog.ErrorDialog
import com.example.mymovieapp.features.splash.domain.usecase.GetUserFromRemoteUseCase
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserFromRemoteUseCase: GetUserFromRemoteUseCase
) : BaseViewModel() {

    private lateinit var navigationCallback : NavigationCallback

    fun setNavigationCallback(navigationCallback: NavigationCallback) {
        this.navigationCallback = navigationCallback
    }

    fun getUserInformation(userUid : String?) {
        getUserFromRemoteUseCase.getUserInformation(userUid)
            .doOnSuccess {
                delay(500L)
                withContext(Dispatchers.Main){
                    navigationCallback.navigateToHomePage()
                }
            }
            .doOnLoading {
                showLoading.value = true
            }
            .doOnError {
                showLoading.value = false
                showDialog.value = ErrorDialog(
                    dialogTag = GET_USER_INFO_FAIL_DIALOG_TAG,
                    titleStrRes = R.string.error,
                    message = it.message ?: "Unexpected Error Occurs.",
                    buttonStrRes = R.string.got_it
                )
            }
            .launchIn(viewModelScope)
    }

    companion object {
        const val GET_USER_INFO_FAIL_DIALOG_TAG = "GET_USER_INFO_FAIL_DIALOG_TAG"
    }

}