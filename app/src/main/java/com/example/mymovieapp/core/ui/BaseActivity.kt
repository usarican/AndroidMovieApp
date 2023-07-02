package com.example.mymovieapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VDB : ViewDataBinding>(): AppCompatActivity() {

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
}