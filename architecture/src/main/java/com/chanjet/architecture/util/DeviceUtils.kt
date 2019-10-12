package com.chanjet.architecture.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.chanjet.architecture.bean.DeviceInfo
import java.io.UnsupportedEncodingException
import java.lang.reflect.InvocationTargetException
import java.util.*

object DeviceUtils {

    private val PREFS_FILE = "gank_device_id.xml"
    private val PREFS_DEVICE_ID = "gank_device_id"
    private var uuid: String? = null
    //    @SuppressLint("MissingPermission")
    //    public static String getDeviceId(Context context) {
    //        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    //        String tmDevice = "" + tm.getDeviceId();
    //        String tmSerial = "" + tm.getSimSerialNumber();
    //        String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    //        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    //        String deviceId = deviceUuid.toString();
    //        String enDeviceId = MD5.getMD5(deviceId);
    //        return enDeviceId;
    //    }
    @SuppressLint("MissingPermission")
    private fun getDeviceId(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }

    fun getDeviceInfo(context: Context): DeviceInfo {
        val deviceInfo = DeviceInfo()
        deviceInfo.language = Locale.getDefault().language
        deviceInfo.local = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
        val d = context.resources.displayMetrics
        deviceInfo.screenHeight = d.heightPixels
        deviceInfo.screenWidth = d.widthPixels
        deviceInfo.dpi = d.densityDpi
        deviceInfo.sdk = Build.VERSION.SDK_INT
        deviceInfo.brand = android.os.Build.BRAND
        deviceInfo.model = Build.MODEL
//        deviceInfo.imei = getDeviceId(context) //待补充 权限申请
        deviceInfo.uuid = getUDID(context)
        deviceInfo.mac = getLocalMacAddressFromWifiInfo(context)
        val guid = MD5.getMD5(deviceInfo.imei + deviceInfo.uuid + deviceInfo.mac)
        deviceInfo.deviceId = guid
        val packageManager = context.packageManager
        try {
            val packageName = context.packageName
            deviceInfo.packageName = packageName
            val appInfo = packageManager
                    .getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)
            val channel = appInfo.metaData.getString("channel")
            val appType = appInfo.metaData.getString("app_type")
            val openServer = appInfo.metaData.getBoolean("open_server", true)
            val gzip_type = appInfo.metaData.getString("accept_encoding", "gzip")
            val encryStatus = appInfo.metaData.getInt("encrypt", 0).toString()
            deviceInfo.channel = channel
            deviceInfo.appType = appType
            deviceInfo.acceptEncoding = gzip_type
            deviceInfo.encrypt = encryStatus
            val info = packageManager.getPackageInfo(packageName, 0)
            deviceInfo.versionCode = info.longVersionCode
            deviceInfo.versionName = info.versionName
        } catch (e: Exception) {
            //ignore e
        }

        return deviceInfo
    }

    private fun getLocalMacAddressFromWifiInfo(context: Context): String {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        return info.macAddress
    }

    /**
     * 获取唯一标识码
     *
     * @param mContext
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun getUDID(mContext: Context): String {
        if (uuid == null) {
            val prefs = mContext.applicationContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            val id = prefs.getString(PREFS_DEVICE_ID, null)
            if (id != null) {
                // Use the ids previously computed and stored in the prefs file
                uuid = id
            } else {
                val androidId = Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
                // Use the Android ID unless it's broken, in which case fallback on deviceId,
                // unless it's not available, then fallback on a random number which we store
                // to a prefs file
                try {
                    if ("9774d56d682e549c" != androidId) {
                        uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8"))).toString()
                    } else {
                        val deviceId = (mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                        uuid = if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))).toString() else UUID.randomUUID().toString()
                    }
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException(e)
                }

                // Write the value out to the prefs file
                prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit()
            }
        }
        return uuid.toString()
    }

    // dip--px
    fun convertDipOrPx(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f * if (dip >= 0) 1 else -1).toInt()
    }

    /**
     *
     * @param context
     * @param op  op 的值是 0 ~ 47，其中0代表粗略定位权限，1代表精确定位权限，24代表悬浮窗权限
     * @return  0 就代表有权限，1代表没有权限，-1函数出错啦
     */
    fun checkOp(context: Context, op: Int): Int {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val `object` = context.getSystemService(Context.APP_OPS_SERVICE)
            val c = `object`!!.javaClass
            try {
                val cArg = arrayOfNulls<Class<*>>(3)
                cArg[0] = Int::class.javaPrimitiveType
                cArg[1] = Int::class.javaPrimitiveType
                cArg[2] = String::class.java
                val lMethod = c.getDeclaredMethod("checkOp", *cArg)
                return lMethod.invoke(`object`, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
        return -1
    }
}
