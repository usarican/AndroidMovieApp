package com.example.mymovieapp.core.ui.event

import android.content.Context
import android.os.Bundle
import com.example.mymovieapp.features.dialog.ErrorDialogFragment
import com.example.mymovieapp.features.dialog.SuccessDialogFragment
import com.example.mymovieapp.utils.ActionType

interface MyEvent

class RetryNetworkCallEvent() : MyEvent

class DialogResultEvent(
    val dialogTag: String,
    val actionType : ActionType? = null,
    val bundleData: Bundle? = null,
    val isChecked : Boolean = false
) : MyEvent

class DialogDismissEvent(
    val dialogTag: String,
    val bundleData: Bundle? = null
) : MyEvent

class OnDestroy(val dialogTag: String, val bundleData: Bundle?) : MyEvent

