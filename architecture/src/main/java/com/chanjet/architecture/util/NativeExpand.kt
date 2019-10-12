package com.chanjet.architecture.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chanjet.architecture.base.BaseApplication
import com.chanjet.architecture.inter.OnBuryClickListener
import com.chanjet.architecture.inter.OnBuryNoParamClickListener
import com.chanjet.architecture.inter.OnInterceptListener

/**
 * Created by liuyicen on 2019-07-05 15:20.
 */

/**
 * @param: 可供方法内部使用
 * @event(): 实际业务
 */

fun <T> View.setOnBuryClickListener(param: T?, event: (v: View, p: T?) -> Unit) {
    setOnClickListener(object : OnBuryClickListener<T>(param) {
        override fun onBuryClick(v: View) {
            this.run { event(v, null) }
        }

        override fun onBuryClick(v: View, param: T) {
            this.run { event(v, param) }
        }
    })
}

/**
 * @param: 可供方法内部使用
 * @event(): 实际业务
 * @OnInterceptListener 单独拦截
 */
fun <T> View.setOnBuryClickListener(param: T?, onInterceptListener: OnInterceptListener, event: (v: View, p: T?) -> Unit) {
    setOnClickListener(object : OnBuryClickListener<T>(param, onInterceptListener) {
        override fun onBuryClick(v: View) {
            this.run { event(v, null) }
        }

        override fun onBuryClick(v: View, param: T) {
            this.run { event(v, param) }
        }
    })
}

/**
 * @event() 实际业务
 */
fun View.setOnBuryClickListener(event: (v: View) -> Unit) {
    setOnClickListener(object : OnBuryNoParamClickListener() {
        override fun onBuryClick(v: View) {
            this.run { event(v) }
        }
    })
}

/**
 * @event(): 实际业务
 * @OnInterceptListener 单独拦截
 */
fun View.setOnBuryClickListener(onInterceptListener: OnInterceptListener, event: (v: View) -> Unit) {
    setOnClickListener(object : OnBuryNoParamClickListener(onInterceptListener) {
        override fun onBuryClick(v: View) {
            this.run { event(v) }
        }
    })
}

fun Any.toast(message: String): Toast {
    return Toast.makeText(BaseApplication.instance, message, Toast.LENGTH_SHORT).apply { show() }
}

fun AppCompatActivity.showToast(message: String) {
    message.toast(message)
}

fun Fragment.showToast(message: String) {
    message.toast(message)
}