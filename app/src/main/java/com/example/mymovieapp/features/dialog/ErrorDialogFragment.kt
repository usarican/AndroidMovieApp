package com.example.mymovieapp.features.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseDialogFragment
import com.example.mymovieapp.core.ui.event.DialogResultEvent
import com.example.mymovieapp.core.ui.event.MyEventManager
import com.example.mymovieapp.core.ui.event.OnDestroy
import com.example.mymovieapp.databinding.FragmentErrorDialogBinding
import com.example.mymovieapp.utils.ActionType
import com.example.mymovieapp.utils.extensions.toGone
import com.example.mymovieapp.utils.extensions.toVisible

class ErrorDialogFragment :
    BaseDialogFragment<FragmentErrorDialogBinding>(R.layout.fragment_error_dialog) {

    private var dialogTag: String = ""
    @StringRes
    private var titleStrRes: Int = R.string.empty
    @StringRes
    private var messageStrRes: Int = R.string.empty

    @StringRes
    private var buttonStrRes: Int = R.string.empty
    private var actionType: ActionType = ActionType.CONFIRM

    @StringRes
    private var button2StrRes: Int = R.string.empty
    private var action2Type: ActionType = ActionType.CANCEL

    private var bundleData: Bundle? = null
    private var showCheckBox = false

    private var cancelableDialog = false
    private var isChecked = false

    private var isDismissAfterAction1 = true
    private var isDismissAfterAction2 = true

    private var isDismissClickOutOfDialog = false


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DIALOG_TAG, dialogTag)
        outState.putInt(TITLE_STRING_RESOURCE, titleStrRes)
        outState.putInt(MESSAGE_STRING_RESOURCE, messageStrRes)
        outState.putInt(BUTTON_STRING_RESOURCE, buttonStrRes)
        outState.putSerializable(ACTION_TYPE, actionType)
        outState.putInt(BUTTON2_STRING_RESOURCE, button2StrRes)
        outState.putSerializable(ACTION2_TYPE, action2Type)
        outState.putBoolean(SHOW_CHECKBOX, showCheckBox)
        outState.putBoolean(IS_CHECKED, isChecked)
        outState.putBoolean(CANCELABLE, isCancelable)
        outState.putBoolean(IS_DISMISS_AFTER_ACTION1, isDismissAfterAction1)
        outState.putBoolean(IS_DISMISS_AFTER_ACTION2, isDismissAfterAction2)
        outState.putBoolean(CANCELABLE, isCancelable)
        outState.putBundle(BUNDLE_DATA, bundleData)
        outState.putBoolean(IS_DISMISS_CLICK_OUT_OF_DIALOG, isDismissClickOutOfDialog)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = savedInstanceState ?: arguments
        bundle?.let {
            dialogTag = it.getString(DIALOG_TAG) ?: ""
            titleStrRes = it.getInt(TITLE_STRING_RESOURCE)
            messageStrRes = it.getInt(MESSAGE_STRING_RESOURCE)
            buttonStrRes = it.getInt(BUTTON_STRING_RESOURCE)
            actionType = it.getSerializable(ACTION_TYPE) as? ActionType ?: ActionType.CONFIRM
            button2StrRes = it.getInt(BUTTON2_STRING_RESOURCE)
            action2Type = it.getSerializable(ACTION2_TYPE) as? ActionType ?: ActionType.CANCEL
            showCheckBox = it.getBoolean(SHOW_CHECKBOX)
            bundleData = it.getBundle(BUNDLE_DATA)
            cancelableDialog = it.getBoolean(CANCELABLE, true)
            isDismissAfterAction1 = it.getBoolean(IS_DISMISS_AFTER_ACTION1, true)
            isDismissAfterAction2 = it.getBoolean(IS_DISMISS_AFTER_ACTION2, true)
            isDismissClickOutOfDialog = it.getBoolean(IS_DISMISS_CLICK_OUT_OF_DIALOG, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = cancelableDialog
        if (binding.buttonAction.visibility == View.GONE && binding.buttonAction2.visibility == View.GONE) {
            this.isCancelable = true
            isDismissClickOutOfDialog = true
            cancelableDialog = true
        }
    }

    override fun setUpUI() {
        binding.textviewDialogHeader.text = getString(titleStrRes).parseAsHtml()
        binding.textviewDialogInformation.text = getString(messageStrRes).parseAsHtml()

        if (buttonStrRes != 0) {
            binding.buttonAction.toVisible()
            binding.buttonAction.text = getString(buttonStrRes).parseAsHtml()
        } else binding.buttonAction.toGone()
        if (button2StrRes != 0) {
            binding.buttonAction2.toVisible()
            binding.buttonAction2.text = getString(button2StrRes).parseAsHtml()
        }
        binding.checkbox.isChecked = isChecked
        if (showCheckBox) {
            binding.checkbox.visibility = View.VISIBLE
        } else {
            binding.checkbox.visibility = View.GONE
        }
    }

    override fun setUpListeners() {
        binding.buttonAction.setOnClickListener {
            MyEventManager.sendEvent(
                DialogResultEvent(
                    dialogTag,
                    actionType,
                    bundleData,
                    isChecked
                )
            )
            if (isDismissAfterAction1) dismiss()
        }
        binding.buttonAction2.setOnClickListener {
            MyEventManager.sendEvent(
                DialogResultEvent(
                    dialogTag,
                    action2Type,
                    bundleData,
                    isChecked,
                )
            )
            if (isDismissAfterAction2) dismiss()
        }

        binding.container.setOnClickListener {
            if (isDismissClickOutOfDialog) {
                dismiss()
            }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        MyEventManager.sendEvent(OnDestroy(dialogTag, bundleData))
        super.onDismiss(dialog)
    }

    companion object {
        private const val DIALOG_TAG = "dialogTag"
        private const val TITLE_STRING_RESOURCE = "titleStringResource"
        private const val MESSAGE_STRING_RESOURCE = "messageStringResource"
        private const val MESSAGE_STRING = "messageString"
        private const val BUTTON_STRING_RESOURCE = "buttonStringResource"
        private const val ACTION_TYPE = "actionType"
        private const val BUTTON2_STRING_RESOURCE = "button2StringResource"
        private const val ACTION2_TYPE = "action2Type"
        private const val BUTTON3_STRING_RESOURCE = "button3StringResource"
        private const val ACTION3_TYPE = "action3Type"
        private const val SHOW_CHECKBOX = "showCheckBox"
        private const val CANCELABLE = "cancelable"
        private const val IS_DISMISS_AFTER_ACTION1 = "isDismissAfterAction1"
        private const val IS_DISMISS_AFTER_ACTION2 = "isDismissAfterAction2"
        private const val IS_DISMISS_AFTER_ACTION3 = "isDismissAfterAction3"
        private const val IS_DISMISS_CLICK_OUT_OF_DIALOG = "isDismissClickOutOfDialog"
        private const val BUNDLE_DATA = "bundleData"
        private const val IS_CHECKED = "isChecked"
        private const val CONTENT_TYPE = "contentType"

        fun newInstance(
            dialogTag: String,
            @StringRes titleStrRes: Int,
            @StringRes messageStrRes: Int,
            @StringRes buttonStrRes: Int,
            actionType: ActionType,
            @StringRes button2StrRes: Int,
            action2Type: ActionType,
            showCheckBox: Boolean = false,
            cancelable: Boolean = true,
            isDismissAfterAction1: Boolean = true,
            isDismissAfterAction2: Boolean = true,
            bundleData: Bundle? = null,
            isDismissClickOutOfDialog: Boolean = false
        ) =
            ErrorDialogFragment().apply {
                arguments = bundleOf(
                    DIALOG_TAG to dialogTag,
                    TITLE_STRING_RESOURCE to titleStrRes,
                    MESSAGE_STRING_RESOURCE to messageStrRes,
                    BUTTON_STRING_RESOURCE to buttonStrRes,
                    ACTION_TYPE to actionType,
                    BUTTON2_STRING_RESOURCE to button2StrRes,
                    ACTION2_TYPE to action2Type,
                    SHOW_CHECKBOX to showCheckBox,
                    CANCELABLE to cancelable,
                    IS_DISMISS_AFTER_ACTION1 to isDismissAfterAction1,
                    IS_DISMISS_AFTER_ACTION2 to isDismissAfterAction2,
                    BUNDLE_DATA to bundleData,
                    IS_DISMISS_CLICK_OUT_OF_DIALOG to isDismissClickOutOfDialog
                )
            }
    }
}