package com.example.mymovieapp.core.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VDB : ViewDataBinding>(private val layoutId : Int ) : DialogFragment(layoutId) {

    private var _binding: VDB? = null

    open val binding get() = _binding!!

    protected open fun setUpViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpUI() {}

    protected open fun setUpObservers() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay)
        this.isCancelable = true
    }

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
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}