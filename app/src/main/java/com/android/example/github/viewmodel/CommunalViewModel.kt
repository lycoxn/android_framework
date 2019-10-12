package com.android.example.github.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.example.github.GithubApp
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.chanjet.architecture.util.toast
import javax.inject.Inject

/**
 * Created by liuyicen on 2019-08-13 18:13.
 */
class CommunalViewModel @Inject constructor() : ViewModel() {

    /**
     * 百度工具类公共方法
     */
    private var mLocationClient: LocationClient? = null//百度
    private val myListener = MyLocationListener()//百度
    private val province: String? = null
    private val addrDetail: String? = null
    private val city: String? = null
    private var mListener: BDloactionListener? = null

    interface BDloactionListener {
        fun finish(bdLocation: com.baidu.location.BDLocation)
        //        void finish(String mProvince, String mCity, String mAddrDetail);
    }

    fun BDLocationUtil(mListener: BDloactionListener) {
        if (mLocationClient == null) {
            this.mListener = mListener
            mLocationClient = LocationClient(GithubApp.instance)// 声明LocationClient类
            val option = LocationClientOption()
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)// 设置定位模式
            option.setCoorType("gcj02")// 返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(3500)// 设置发起定位请求的间隔时间为5000ms
            option.setIsNeedAddress(true)// 返回的定位结果包含地址信息
            option.setNeedDeviceDirect(false)// 返回的定位结果包含手机机头的方向
            option.setAddrType("all")
            option.setOpenGps(true) // 打开gps
            option.setIsNeedLocationDescribe(true) // 是否需要位置描述信息，默认为不需要
            mLocationClient!!.setLocOption(option)
            mLocationClient!!.registerLocationListener(myListener) // 注册监听函数
            mLocationClient!!.start()
        } else {
            toast("buweikong")
        }
    }

    internal inner class MyLocationListener : BDAbstractLocationListener() {
        var first: Boolean = false

        override fun onReceiveLocation(bdLocation: com.baidu.location.BDLocation?) {
            if (!first && bdLocation != null) {
                if (mListener != null) {
                    mListener!!.finish(bdLocation)
                    first = true
                } else {
                    first = false
                }
            }
        }
    }

    fun stopLocation() {
        if (mLocationClient != null)
            try {
                mLocationClient?.let {
                    it.stop()
                    it.unRegisterLocationListener(myListener)
                }
            } catch (e: Exception) {
            }
    }


}