package com.example.mymovieapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.mymovieapp.utils.StringProvider
import javax.inject.Inject
abstract class BaseFragment<VDB : ViewDataBinding>(private val layoutId : Int ) : Fragment(layoutId) {

    @Inject
    protected lateinit var stringProvider : StringProvider

    private var _binding: VDB? = null

    open val binding get() = _binding!!

    protected var viewWeWantToGiveMargin : View? = null

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


    protected open fun handleToolbarOverlaps(viewWeWantToGiveMargin: View?) {
        activity?.window?.decorView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
                // Apply the insets as a margin to the view. Here the system is setting
                // only the bottom, left, and right dimensions, but apply whichever insets are
                // appropriate to your layout. You can also update the view padding
                // if that's more appropriate.
                viewWeWantToGiveMargin?.let { viewWeWantToGiveMargin ->
                    viewWeWantToGiveMargin.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        leftMargin = insets.left
                        bottomMargin = insets.bottom
                        rightMargin = insets.right
                        topMargin = insets.top
                    }
                }

                // Return CONSUMED if you don't want want the window insets to keep being
                // passed down to descendant views.
                ViewCompat.onApplyWindowInsets(view, windowInsets)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpObservers()
    }

    open fun showLoadingDialog(isShow : Boolean){
        val baseActivity = requireActivity()
        if (baseActivity is BaseActivity<*>){
            baseActivity.showLoadingDialog(isShow)
        }
    }

    open fun setBaseViewModel(viewModel: BaseViewModel){
        val baseActivity = requireActivity()
        if (baseActivity is BaseActivity<*>){
            baseActivity.setBaseViewModel(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}