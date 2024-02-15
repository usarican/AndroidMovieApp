package com.example.mymovieapp.features.dialog

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentDialog
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseDialogFragment
import com.example.mymovieapp.core.ui.event.DialogDismissEvent
import com.example.mymovieapp.core.ui.event.DialogResultEvent
import com.example.mymovieapp.core.ui.event.MyEventManager
import com.example.mymovieapp.databinding.FragmentSuccessDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SuccessDialogFragment : BaseDialogFragment<FragmentSuccessDialogBinding>(R.layout.fragment_success_dialog) {

    private var dialogTag: String = ""
    @StringRes private var title: Int = 0
    @StringRes private var message: Int = 0
    private var timeoutMs: Long = -1
    private var isCancellable: Boolean = true
    private var bundleData: Bundle? = null

    @StringRes private var buttonStrRes: Int = R.string.empty

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DIALOG_TAG, dialogTag)
        outState.putInt(TITLE, title)
        outState.putInt(MESSAGE, message)
        outState.putLong(DISMISS_TIMEOUT, timeoutMs)
        outState.putBoolean(IS_CANCELABLE, isCancellable)
        outState.putBundle(BUNDLE_DATA, bundleData)
        outState.putInt(BUTTON_STRING_RESOURCE, buttonStrRes)
        super.onSaveInstanceState(outState)
    }

    override fun setUpViews(view: View, savedInstanceState: Bundle?) {
        super.setUpViews(view, savedInstanceState)
        val bundle = savedInstanceState ?: arguments
        bundle?.let {
            dialogTag = it.getString(DIALOG_TAG, "")
            title = it.getInt(TITLE, 0)
            message = it.getInt(MESSAGE, 0)
            bundleData = it.getBundle(BUNDLE_DATA)
            isCancellable = it.getBoolean(IS_CANCELABLE)
            timeoutMs = it.getLong(DISMISS_TIMEOUT, -1)
            buttonStrRes = it.getInt(BUTTON_STRING_RESOURCE)
        }
    }

    override fun setUpUI(){
        binding.textviewDialogHeader.text = getString(title)
        binding.buttonAction.text = getString(buttonStrRes)
        binding.textviewDialogInformation.text = getString(message)
        this.isCancelable = isCancellable
    }

    override fun setUpListeners() {
        binding.buttonAction.setOnClickListener {
            MyEventManager.sendEvent(
                DialogResultEvent(
                    dialogTag = dialogTag,
                    bundleData = bundleData,
                )
            )
            dismiss()
        }
        binding.container.setOnClickListener { dismiss() }
    }

    override fun dismiss() {
        MyEventManager.sendEvent(DialogDismissEvent(dialogTag, bundleData))
        super.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (timeoutMs != -1L) {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(timeoutMs)
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        try {
                            dismiss()
                        } catch (ignore: Exception) {
                        }
                    }
                }
            }
        }

        (dialog as ComponentDialog)
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dismiss()
                }
            })
    }

    companion object {
        private const val DIALOG_TAG = "dialogTag"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val MESSAGE_STR = "messageStr"
        private const val CONTENT_TYPE = "contentType"
        private const val DISMISS_TIMEOUT = "dismissTimeOut"
        private const val IS_CANCELABLE = "isCancelable"
        private const val BUNDLE_DATA = "bundleData"
        private const val BUTTON_STRING_RESOURCE = "buttonStringResource"
        private const val ACTION_TYPE = "actionType"

        fun newInstance(
            dialogTag: String,
            @StringRes title: Int,
            @StringRes message: Int,
            dismissTimeOut: Long = -1,
            isCancelable: Boolean = false,
            bundleData: Bundle? = null,
            @StringRes buttonStrRes: Int,
        ) = SuccessDialogFragment().apply {
            arguments = bundleOf(
                DIALOG_TAG to dialogTag,
                TITLE to title,
                MESSAGE to message,
                DISMISS_TIMEOUT to dismissTimeOut,
                IS_CANCELABLE to isCancelable,
                BUNDLE_DATA to bundleData,
                BUTTON_STRING_RESOURCE to buttonStrRes,
            )
        }
    }
}