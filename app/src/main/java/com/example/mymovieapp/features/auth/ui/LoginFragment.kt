package com.example.mymovieapp.features.auth.ui

import android.app.Activity.RESULT_OK
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentLoginBinding
import com.example.mymovieapp.utils.Constants.FACEBOOK_LOGIN_PERMISSIONS
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: AuthenticationViewModel by activityViewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    @Inject
    lateinit var callbackManager: CallbackManager
    @Inject
    lateinit var loginManager: LoginManager

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == RESULT_OK && data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    viewModel.signInWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Timber.tag(TAG).w("Google sign in failed $e")
                }
            }
        }



    override fun setUpListeners() {
        binding.apply {
            loginButtonSignIn.setOnClickListener {
                userLogIn(
                    userEmail = textinputEmail.editText?.text.toString(),
                    userPassword = textinputPassword.editText?.text.toString()
                )
            }
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            loginWithGoogleButton.setOnClickListener {
                googleSignIn()
            }
            loginWithFacebookButton.setOnClickListener {
                signInWithFacebook()
            }
        }
    }

    override fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.getAuthUserDataStateFlow().collectLatest {
                binding.textinputEmail.editText?.setText(it.userMail)
            }
        }
        viewModel.userEnteredOnce().observe(viewLifecycleOwner) { userEnteredOnce ->
            userEnteredOnce?.let {
                if (it) {
                    viewModel.setUserEnteredOnceToNull()
                    val action = LoginFragmentDirections.actionLoginFragmentToSetupProfileFragment()
                    findNavController().navigate(action)
                } else {
                    val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun setUpUI() {
        viewModel.resetViewPagerCurrentPage()
    }

    private fun userLogIn(userEmail: String, userPassword: String) {
        viewModel.signInWithFirebaseEmailAndPassword(userEmail, userPassword)
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun signInWithFacebook(){
        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.tag("FacebookB").d("facebook:onSuccess:$loginResult")
                    viewModel.signInWithFacebook(loginResult.accessToken.token)
                }

                override fun onCancel() {
                    Timber.tag("FacebookB").d( "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Timber.tag("FacebookB").d( "facebook:onError $error" )
                }
            },
        )
        loginManager.logInWithReadPermissions(this@LoginFragment,callbackManager,FACEBOOK_LOGIN_PERMISSIONS)
    }


    companion object {
        private const val TAG = "GoogleActivity"
    }
}