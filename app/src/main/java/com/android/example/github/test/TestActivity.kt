package com.android.example.github.test

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.android.example.github.GithubApp
import com.android.example.github.R
import com.android.example.github.databinding.ActivityTestBinding
import com.chanjet.architecture.base.BaseActivity
import com.chanjet.architecture.base.BasePresenter
import com.chanjet.architecture.bean.DeviceInfo
import com.chanjet.architecture.di.Injectable
import com.chanjet.architecture.inter.OnLeakCanaryListener
import com.chanjet.architecture.provider.StaticProvider
import com.chanjet.architecture.util.setOnBuryClickListener
import com.chanjet.architecture.util.showToast
import kotlinx.android.synthetic.main.activity_test.*
import timber.log.Timber
import javax.inject.Inject

/**
 * @Injectable  ViewController是Activity需实现此接口
 * @HasSupportFragmentInjector  ViewController是Fragment需在此实现此接口
 * Created by liuyicen on 2019-07-03 15:23.
 */
class TestActivity : BaseActivity<TestViewModel, ActivityTestBinding>(), Injectable {
    override fun getPresenter(): BasePresenter = TestPresenter(GithubApp.instance!!.applicationContext)

    override fun getViewModel(): TestViewModel = ViewModelProviders.of(this, viewModelFactory).get(TestViewModel::class.java)

    override fun getLayoutId(): Int = R.layout.activity_test

    @Inject
    lateinit var device: DeviceInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        text?.setOnBuryClickListener<Any>("1111111") { _, param -> Toast.makeText(this@TestActivity, param.toString(), Toast.LENGTH_SHORT).show() }
        text2?.setOnBuryClickListener { Toast.makeText(this@TestActivity, mViewModel.getData(), Toast.LENGTH_SHORT).show() }
        mToolbar?.setTitle("asdasd")

        StaticProvider.instance.setOnInterceptListener("OnLeakCanaryListener1")
        text2?.setOnBuryClickListener(object : OnLeakCanaryListener() {
            override fun onIntercept() {
                super.onIntercept()
                println("OnInterceptListener11111111111111")
            }
        }) { Toast.makeText(this@TestActivity, mViewModel.getData(), Toast.LENGTH_SHORT).show() }
        text.setOnClickListener { pre.showWaitingDialog("2222") }
        text.setOnClickListener { showToast("123456") }
        text.setOnClickListener {
            showToast(device.isDebug.toString())
            Timber.d("Cannot convert  to number")
        }
    }


}
