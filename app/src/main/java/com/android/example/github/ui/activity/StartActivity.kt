package com.android.example.github.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.android.example.github.api.GithubService
import com.android.example.github.api.YunService
import com.android.example.github.databinding.ActivityStartBinding
import com.android.example.github.dto.GetClientPrivateKey
import com.android.example.github.dto.GetClientPrivateKeyBean
import com.android.example.github.proutil.Config
import com.android.example.github.proutil.EncryptUtil
import com.android.example.github.proutil.SystemToolUtils
import com.android.example.github.proutil.rsa.RSAEncrypt
import com.android.example.github.test.TestPresenter
import com.android.example.github.viewmodel.CommunalViewModel
import com.architect.outside_core.baidumap.BDLocationUtil
import com.baidu.location.BDLocation
import com.chanjet.architecture.api.CommonObserver
import com.chanjet.architecture.base.BaseActivity
import com.chanjet.architecture.base.BasePresenter
import com.chanjet.architecture.di.Injectable
import com.chanjet.architecture.util.showToast
import com.chanjet.architecture.util.toast
import kotlinx.android.synthetic.main.activity_start.*
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by liuyicen on 2019-08-13 11:25.
 */
class StartActivity : BaseActivity<CommunalViewModel, ActivityStartBinding>(), Injectable, EasyPermissions.PermissionCallbacks {

    private var notification: String? = null

    @Inject
    lateinit var githubService: GithubService

    @Inject
    lateinit var yunService: YunService

    @Inject
    lateinit var test: Test
    private var locationUtil: BDLocationUtil? = null

    override fun getLayoutId(): Int = com.android.example.github.R.layout.activity_start

    override fun getViewModel(): CommunalViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommunalViewModel::class.java)

    override fun getPresenter(): BasePresenter = TestPresenter(this)

    override fun getOverrideContentView() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        checkPermission()
//        notification = intent.getStringExtra(Constant.NOTIFICATION)
        bt.setOnClickListener {
            //            initBDSDK()
//            getClientPrivateKey(bdLocation.longitude, bdLocation.latitude)
            //            showCameraWithPermissionCheck()
//            val map = mapOf("deviceNo" to "111", "sessionId" to "aaa", "reqData" to mapOf("tokenType" to "10"), "sign" to "asd")
//            githubService.getToken(map).observe(this, Observer<ApiResponse<RepoSearchResponse>> { t -> showToast(t.toString()) })
        }
    }


    private fun initBDSDK() {
        locationUtil = BDLocationUtil(this, object : BDLocationUtil.BDloactionListener {
            internal var hasLocation = false

            override fun finish(bdLocation: BDLocation?) {
                if (bdLocation != null && bdLocation.locType != BDLocation.TypeServerError) {
                    //环境监测
                    locationUtil?.stopLocation()
                    if (!hasLocation) {
//                        LogUtils.show("111111百度回调r", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
                        hasLocation = true
                        getClientPrivateKey(bdLocation.longitude, bdLocation.latitude)
                    }
                } else {
                    toast("地址获取失败，请退出重试！")
                }
            }
        })
    }

    private fun checkPermission() {
        if (!EasyPermissions.hasPermissions(this@StartActivity, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this@StartActivity,
                    "为了不影响您的使用需要需要获取您的位置以及手机版本信息",
                    0,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else initBDSDK()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showToast("为了不影响您的使用需要需要获取您的位置以及手机版本信息")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initBDSDK()
    }

    private fun getClientPrivateKey(longitude: Double, latitude: Double) {

//        test.show()
//        showToast(deviceInfo.isDebug.toString())
        val map = mapOf("deviceNo" to SystemToolUtils.getPhoneIMEI(),
                "osType" to "1",
                "appVersion" to SystemToolUtils.getAppVersionName(this),
                "masterKey" to RSAEncrypt.encrypt(Config.getDes_key()),
                "applicationType" to "4",
                "sign" to EncryptUtil.desEncrypt(SystemToolUtils.getPhoneIMEI(), Config.getDes_key()))
        yunService.getClientPrivateKey(map).observe(this, object : CommonObserver<GetClientPrivateKey>(pre) {
            override fun onComplete(t: GetClientPrivateKey?) {
                showToast(t!!.key)
            }
        })
    }

//    @AfterPermissionGranted(0)
//    private fun methodRequiresTwoPermission() {
//        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
//        if (EasyPermissions.hasPermissions(this, *perms)) {
//            // Already have permission, do the thing
//            // ...
//            showToast("同意权限1")
//
//        } else {
//            // Do not have permissions, request them now
//            showToast("申请权限1")
//        }
//    }
}