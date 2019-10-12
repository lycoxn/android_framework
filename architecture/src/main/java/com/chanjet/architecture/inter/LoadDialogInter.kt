package com.chanjet.architecture.inter

/**
 * Created by liuyicen on 2019-07-03 15:45.
 */
interface LoadDialogInter {
    fun showWaitingDialog()

    fun showWaitingDialog(message: String)

    fun dismissWaitingDialog()

}