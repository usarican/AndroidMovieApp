package com.example.mymovieapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.mymovieapp.features.dialog.LoadingDialog
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding>(): AppCompatActivity() {

    @Inject
    lateinit var loadingDialog : LoadingDialog

    open val binding : VDB by lazy {  bindingFactory(layoutInflater) }

    abstract fun bindingFactory(layoutInflater: LayoutInflater) : VDB

    protected open fun setUpViews(savedInstanceState: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpObservers() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setUpViews(savedInstanceState)
        setUpListeners()
        setUpObservers()
    }

    fun setBaseViewModel(viewModel: BaseViewModel){
        viewModel.showLoading.observe(this){ showLoading ->
            showLoading?.let {
                showLoadingDialog(it)
            }
        }
    }

    open fun showLoadingDialog(showLoading : Boolean){
        if (showLoading) loadingDialog.showLoadingDialog() else loadingDialog.hideLoadingDialog()
    }
}