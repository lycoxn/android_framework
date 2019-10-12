package com.android.example.github.test

import android.content.Context
import com.chanjet.architecture.base.BaseDialog
import com.chanjet.architecture.base.BasePresenter

/**
 * Created by liuyicen on 2019-07-12 16:13.
 */
class TestPresenter(context: Context) : BasePresenter(context) {

    override fun getDialogWait(context: Context): BaseDialog<*> = TestDialog(context)
}