package com.architect.outside_core.baidumap;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by admin on 2018/12/14
 */
public class BDLocationUtil {
    private LocationClient mLocationClient = null;//百度
    private BDAbstractLocationListener myListener = new MyLocationListener();//百度
    private String province, addrDetail, city;
    private BDloactionListener mListener;

    public interface BDloactionListener {
        void finish(com.baidu.location.BDLocation bdLocation);
//        void finish(String mProvince, String mCity, String mAddrDetail);
    }

    public BDLocationUtil(Context context, BDloactionListener mListener) {
        if (mLocationClient == null) {
            this.mListener = mListener;
            mLocationClient = new LocationClient(context.getApplicationContext());// 声明LocationClient类
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
            option.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(3500);// 设置发起定位请求的间隔时间为5000ms
            option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
            option.setNeedDeviceDirect(false);// 返回的定位结果包含手机机头的方向
            option.setAddrType("all");
            option.setOpenGps(true); // 打开gps
            option.setIsNeedLocationDescribe(true); // 是否需要位置描述信息，默认为不需要
            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(myListener); // 注册监听函数
            mLocationClient.start();
        } else {
            Toast.makeText(context.getApplicationContext(), "buweikong", Toast.LENGTH_SHORT).show();
        }
    }

    class MyLocationListener extends BDAbstractLocationListener {
        boolean first;

        @Override
        public void onReceiveLocation(com.baidu.location.BDLocation bdLocation) {
            if (!first && bdLocation != null) {
                if (mListener != null) {
                    mListener.finish(bdLocation);
                    first = true;
                } else {
                    first = false;
                }
            }
        }
    }

    public void stopLocation() {
        if (mLocationClient != null)
            try {
                mLocationClient.stop();
                mLocationClient.unRegisterLocationListener(myListener);
            } catch (Exception e) {
            }
    }

}
