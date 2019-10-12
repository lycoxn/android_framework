package com.chanjet.architecture.base

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDexApplication
//import com.squareup.leakcanary.LeakCanary.*
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by liuyicen on 2019-07-05 11:16.
 */
abstract class BaseApplication : MultiDexApplication(), HasActivityInjector, HasBroadcastReceiverInjector, HasServiceInjector, HasContentProviderInjector,
        HasSupportFragmentInjector {

    //初始化DaggerAndroid
    @Inject
    @JvmField
    var activityInjector: DispatchingAndroidInjector<Activity>? = null
    @Inject
    @JvmField
    var supportFragmentInjector: DispatchingAndroidInjector<Fragment>? = null
    @Inject
    @JvmField
    var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>? = null
    @Inject
    @JvmField
    var serviceInjector: DispatchingAndroidInjector<Service>? = null
    @Inject
    @JvmField
    var contentProviderInjector: DispatchingAndroidInjector<ContentProvider>? = null

    override fun activityInjector() = activityInjector

    override fun broadcastReceiverInjector() = broadcastReceiverInjector

    override fun serviceInjector() = serviceInjector

    override fun contentProviderInjector() = contentProviderInjector

    override fun supportFragmentInjector() = supportFragmentInjector

    //初始化注入方式和LeakCanary
    override fun onCreate() {
        super.onCreate()
        instance = this
//        if (isInAnalyzerProcess(this)) {
//            //此过程专用于LeakCanary进行堆分析。在此过程中不应初始化应用程序。
//            return
//        }
//        install(this)
    }

    companion object {
        var instance: BaseApplication? = null
    }

}