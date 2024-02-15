package com.example.mymovieapp.features.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.mymovieapp.core.ui.MyDialog
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.utils.ActionType
import com.example.mymovieapp.utils.MyClickListeners

class BannerMovieDetailDialog(
    val movieDetailItem: MovieDetail,
    private val clickListeners: MyClickListeners<Int>
) : MyDialog {
    override fun create(): DialogFragment {
        val fragment = BannerMovieItemDetailDialogFragment.newInstance(
            movieDetailItem = movieDetailItem
        )
        fragment.setClickListener(clickListeners)
        return fragment
    }
}

class ErrorDialog(
    private val dialogTag: String,
    @StringRes private val titleStrRes: Int,
    @StringRes private val messageStrRes: Int,
    @StringRes private val buttonStrRes: Int,
    private val actionType: ActionType,
    @StringRes private val button2StrRes: Int,
    private val action2Type: ActionType,
    private var showCheckBox: Boolean = false,
    private var cancelable: Boolean = true,
    private var isDismissAfterAction1: Boolean = true,
    private var isDismissAfterAction2: Boolean = true,
    private var bundleData: Bundle? = null,
    private var isDismissClickOutOfDialog: Boolean = false
) : MyDialog {
    override fun create(): DialogFragment =
        ErrorDialogFragment.newInstance(
            dialogTag,
            titleStrRes,
            messageStrRes,
            buttonStrRes,
            actionType,
            button2StrRes,
            action2Type,
            showCheckBox,
            cancelable,
            isDismissAfterAction1,
            isDismissAfterAction2,
            bundleData,
            isDismissClickOutOfDialog
        )


}

class SuccessDialog(
    private val dialogTag: String,
    @StringRes private val title: Int,
    @StringRes private val message: Int,
    private var dismissTimeOut: Long = -1,
    private var isCancelable: Boolean = false,
    private var bundleData: Bundle? = null,
    @StringRes private val buttonStrRes: Int,
) : MyDialog {
    override fun create(): DialogFragment =
        SuccessDialogFragment.newInstance(
            dialogTag,
            title,
            message,
            dismissTimeOut,
            isCancelable,
            bundleData,
            buttonStrRes
        )
}
