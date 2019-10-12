package com.chanjet.architecture.bean

/**
 * 设备信息
 */
class DeviceInfo {

    var packageName: String? = null//包名
    var local: String? = null//时区
    var screenHeight: Int = 0//屏幕高度
    var screenWidth: Int = 0//屏幕宽度
    var dpi: Int = 0//屏幕密度
    var sdk: Int = 0//操作系统版本
    var brand: String? = null//手机品牌
    var model: String? = null//设备型号
    var deviceId: String? = null//设备唯一标示
    var language: String? = null//语言
    var versionCode: Long = 0//应用版本号
    var versionName: String? = null//应用版本名称
    var channel: String? = null
        set(channel) {
            field = channel
            if ("dev" == channel)
                isDebug = true
        }//应用渠道
    var appType: String? = null//应用类型

    var imei: String? = null
    var mac: String? = null
    var uuid: String? = null
    var platform: String? = null////平台:android,iphone,ipad

    var acceptEncoding: String? = null// 1 压缩,0 不压缩 //控制返回结果是否启用压缩
    var encrypt: String? = null//1加密, 0 非加密，返回结果是否启用加密,加密类型与当前客户端请求加密方式及加密版本保持一致

    //    var isOpenServer: Boolean = false
    var isDebug: Boolean = false
}
