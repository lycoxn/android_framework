package com.chanjet.architecture.inter

import android.view.View
import com.chanjet.architecture.provider.StaticProvider

/**
 * Created by liuyicen on 2019-07-05 18:54.
 */
abstract class OnBuryClickListener<T> : View.OnClickListener {
    private var param: T? = null
    private var listener: OnInterceptListener? = null

    //默认拦截
//    constructor() {
//        listener = StaticProvider.instance.getOnInterceptListener()
//    }

    //不用拦截
    constructor(t: T?) {
        this.param = t
    }

    //无参  自定义拦截（覆盖原拦截方法,可实现onMore补充拦截）
//    constructor(listener: OnInterceptListener) {
//        this.listener = listener
//    }

    //带参  自定义拦截（覆盖原拦截方法,可实现onMore补充拦截）
    constructor(t: T?, listener: OnInterceptListener) {
        this.param = t
        this.listener = listener
    }

    override fun onClick(v: View) {
        listener?.onIntercept()
        listener?.onMore()
        param?.let {
            onBuryClick(v, it)
        }
        param ?: onBuryClick(v)
    }

    abstract fun onBuryClick(v: View, param: T)

    abstract fun onBuryClick(v: View)
}

abstract class OnBuryNoParamClickListener : View.OnClickListener {
    private var listener: OnInterceptListener? = null

    //默认拦截
    constructor() {
        listener = StaticProvider.instance.getOnInterceptListener()
    }

    //自定义拦截（覆盖原拦截方法,可实现onMore补充拦截）
    constructor(listener: OnInterceptListener) {
        this.listener = listener
    }

    override fun onClick(v: View) {
        listener?.onIntercept()
        listener?.onMore()
        onBuryClick(v)
    }

    abstract fun onBuryClick(v: View)
}

