package com.chanjet.architecture.inter


/**
 * Created by liuyicen on 2019-07-05 14:24.
 */
open class OnInterceptListener {

    open fun onIntercept() {}

    open fun onMore() {}
}

open class OnLeakCanaryListener : OnInterceptListener() {
    override fun onIntercept() {
        println("OnInterceptListener---LeakCanary")
    }
}

open class OnLeakCanaryListener1 : OnInterceptListener() {
    override fun onIntercept() {
        println("OnInterceptListener1111---LeakCanary")
    }
}