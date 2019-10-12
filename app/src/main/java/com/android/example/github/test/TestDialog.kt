package com.android.example.github.test

import android.content.Context
import android.content.DialogInterface
import com.android.example.github.R
import com.android.example.github.databinding.DialogTestBinding
import com.chanjet.architecture.base.BaseDialog

/**
 * Created by liuyicen on 2019-07-12 16:14.
 */
class TestDialog(context: Context) : BaseDialog<DialogTestBinding>(context) {
    override fun onDismiss(dialog: DialogInterface?) {

    }

    override fun setMessage(msg: String?) {
        mBinding.text.text = msg
    }

    override fun getLayoutId(): Int = R.layout.dialog_test
}