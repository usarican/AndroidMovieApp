package com.example.mymovieapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VDB : ViewDataBinding>(private val layoutId : Int ) : Fragment(layoutId) {

    private var _binding: VDB? = null

    open val binding get() = _binding!!

    protected open fun setUpViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpUI() {}

    protected open fun setUpObservers() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setUpViews(binding.root, savedInstanceState)
        setUpUI()
        setUpListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}