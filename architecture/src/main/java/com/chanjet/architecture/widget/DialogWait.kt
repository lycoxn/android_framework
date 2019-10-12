package com.chanjet.architecture.widget

import android.content.Context
import android.content.DialogInterface

import com.chanjet.architecture.R
import com.chanjet.architecture.base.BaseDialog
import com.chanjet.architecture.databinding.DialogWaitingBinding

open class DialogWait(context: Context) : BaseDialog<DialogWaitingBinding>(context, R.style.DialogStyle) {

    override fun getLayoutId(): Int = R.layout.dialog_waiting

    public override//    @Deprecated
    fun setMessage(message: String) {
        mBinding.message = message
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
    }
}
