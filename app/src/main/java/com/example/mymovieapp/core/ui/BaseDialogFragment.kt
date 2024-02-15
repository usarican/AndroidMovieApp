package com.example.mymovieapp.core.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.mymovieapp.R

abstract class BaseDialogFragment<VDB : ViewDataBinding>(private val layoutId : Int ) : DialogFragment(layoutId) {

    private var _binding: VDB? = null

    open val binding get() = _binding!!

    protected open fun setUpViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpUI() {}

    protected open fun setUpObservers() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Dark_TransparentNavbar)
        this.isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            setDimAmount(0.5F)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
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