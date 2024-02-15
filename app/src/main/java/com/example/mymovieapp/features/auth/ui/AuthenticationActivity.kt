package com.example.mymovieapp.features.auth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseActivity
import com.example.mymovieapp.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {

    val viewModel : AuthenticationViewModel by viewModels()
    override fun bindingFactory(layoutInflater: LayoutInflater): ActivityAuthenticationBinding {
        return ActivityAuthenticationBinding.inflate(layoutInflater)
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        setBaseViewModel(viewModel)
    }

}