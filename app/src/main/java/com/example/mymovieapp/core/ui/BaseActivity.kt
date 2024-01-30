package com.example.mymovieapp.core.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.mymovieapp.features.dialog.DialogManager
import com.example.mymovieapp.features.dialog.LoadingDialog
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding>() : AppCompatActivity() {

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var dialogManager: DialogManager

    open val binding: VDB by lazy { bindingFactory(layoutInflater) }

    abstract fun bindingFactory(layoutInflater: LayoutInflater): VDB

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

    fun setBaseViewModel(viewModel: BaseViewModel) {
        viewModel.showLoading.observe(this) { showLoading ->
            showLoading?.let {
                showLoadingDialog(it)
            }
        }
        viewModel.showDialog.observe(this) { myUIComponents ->
            myUIComponents?.let {
                showUIComponent(myUIComponents)
                viewModel.showDialog.value = null
            }
        }

    }

    open fun showLoadingDialog(showLoading: Boolean) {
        if (showLoading) loadingDialog.showLoadingDialog() else loadingDialog.hideLoadingDialog()
    }

    open fun showUIComponent(component: MyUIComponents) {
        when (component) {
            is MyActivity -> {}
            is MyDialog -> dialogManager.showDialogFragment(component)
        }
    }
}