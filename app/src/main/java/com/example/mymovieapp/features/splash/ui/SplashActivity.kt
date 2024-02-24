package com.example.mymovieapp.features.splash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymovieapp.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (auth.currentUser != null) {
            Timber.tag("Splash").d(auth.toString())
        }
    }
}