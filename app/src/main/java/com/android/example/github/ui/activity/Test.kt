package com.android.example.github.ui.activity

import com.chanjet.architecture.bean.DeviceInfo
import com.chanjet.architecture.util.toast

/**
 * Created by liuyicen on 2019-09-05 11:12.
 */
class Test(private val deviceInfo: DeviceInfo) {

    fun show() {
        toast(deviceInfo.isDebug.toString())
    }
}