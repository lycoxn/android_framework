package com.chanjet.architecture.provider

import com.chanjet.architecture.Constants.LEAKCANARY_LISTENER
import com.chanjet.architecture.inter.OnInterceptListener
import com.chanjet.architecture.inter.OnLeakCanaryListener
import com.chanjet.architecture.inter.OnLeakCanaryListener1

/**
 * Created by liuyicen on 2019-07-10 17:28.
 */

class StaticProvider private constructor() : StaticProviderInter {

    private var onInterceptListener: OnInterceptListener? = null

    override fun getOnInterceptListener(): OnInterceptListener? = onInterceptListener

    fun setOnInterceptListener(s: String) {
        this.onInterceptListener = StaticProviderFactory().create(s)
    }

    companion object {
        val instance: StaticProvider by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            StaticProvider()
        }
    }

}

interface StaticProviderInter {
    fun getOnInterceptListener(): OnInterceptListener?
}

class StaticProviderFactory {
    fun create(tag: String): OnInterceptListener {
        when (tag) {
            LEAKCANARY_LISTENER -> return OnLeakCanaryListener()
            "OnLeakCanaryListener1" -> return OnLeakCanaryListener1()
        }
        return OnLeakCanaryListener()
    }
}
