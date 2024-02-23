package com.example.mymovieapp.core.di

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.example.mymovieapp.utils.Constants.GOOGLE_WEB_CLIENT_ID
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object FirebaseModule {

    @Provides
    fun provideGoogleSignInOptions() : GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()

    @Provides
    fun provideGoogleSignInClient(
        fragmentActivity: FragmentActivity,
        googleSignInOptions: GoogleSignInOptions
    ) : GoogleSignInClient =  GoogleSignIn.getClient(fragmentActivity, googleSignInOptions)

    @Provides
    fun provideFacebookCallbackManager() : CallbackManager =
        CallbackManager.Factory.create()

    @Provides
    fun provideFacebookLoginManager() : LoginManager =
        LoginManager.getInstance()

}