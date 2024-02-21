package com.example.mymovieapp.features.auth.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Arrays
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: AuthenticationViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        auth = Firebase.auth

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
        buttonFacebookLogin = binding.loginButton

        buttonFacebookLogin.setPermissions("email", "public_profile")
        buttonFacebookLogin.setFragment(this)
        buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.tag(TAG).d("facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Timber.tag(TAG).d( "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Timber.tag(TAG).d( "facebook:onError $error" )
                }
            },
        )
    }

    private fun userLogIn(userEmail: String, userPassword: String) {
        viewModel.signInWithFirebaseEmailAndPassword(userEmail, userPassword)
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.tag(TAG).d("handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(TAG).d("signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(TAG).w("signInWithCredential:failure ${task.exception}")
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}