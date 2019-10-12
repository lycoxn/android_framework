package com.chanjet.architecture.base

import android.content.Context
import com.chanjet.architecture.inter.LoadDialogInter
import com.chanjet.architecture.widget.DialogWait

/**
 * Created by liuyicen on 2019-07-12 15:22.
 */
open class BasePresenter(context: Context) : LoadDialogInter {

    private val mDialogWait: BaseDialog<*> = getDialogWait(context)

    open fun getDialogWait(context: Context): BaseDialog<*> {
        return DialogWait(context)
    }

    override fun showWaitingDialog(message: String) {
        mDialogWait.setMessage(message)
        mDialogWait.showDialog()
    }

    override fun dismissWaitingDialog() {
        if (mDialogWait.isShowing) {
            mDialogWait.dismiss()
        }
    }

    override fun showWaitingDialog() {
        mDialogWait.showDialog()
    }

}